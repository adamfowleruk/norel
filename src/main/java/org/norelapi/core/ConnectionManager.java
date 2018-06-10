/*! ******************************************************************************
 *
 * NoREL API
 *
 * Copyright (C) 2018 by Adam Fowler : http://www.adamfowler.org/
 *
 *******************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/
package org.norelapi.core;

import java.util.Hashtable;
import java.util.TreeSet;
import java.io.File;
import java.io.InputStream;
import java.io.IOException;

/**
 * This is the first class you should use when gaining a connection to a NoSQL database
 *
 * By default, only drivers from our package are loaded. Use the addDriversFromClasspath() method to discover third party drivers
 * that support the NoREL driver API.
 *
 * Connection configuration information can be loaded from json files, or json files in the classpath. These are not loaded automatically.
 *
 * Once you have drivers and individual connection configuration loaded, you can request a connection instance.
 */
public class ConnectionManager {
  private Hashtable<String,DriverInfo> driverInfos = new Hashtable<String,DriverInfo>();

  private Hashtable<String,Driver> drivers = new Hashtable<String,Driver>();

  private Hashtable<String,ConnectionInfo> connectionInfos = new Hashtable<String,ConnectionInfo>();

  private TreeSet<Connection> connections = new TreeSet<Connection>();

  public ConnectionManager() {
    discoverDriversFromClasspath();
    discoverConnectionInfoFromClasspath();
  }

  public void removeAllDriverInfos() {
    driverInfos.removeAll();
    // TODO security/auditability - do not remove any in use
  }

  public void removeAllConnectionInfos() {
    connectionInfos.removeAll();
    // TODO security/auditability - do not remove any in use
  }

  public void discoverDriversFromClasspath() {
    // look just for a norelapi.drivers.default.json within the package, and norelapi.drivers.json at root, for now
    InputStream rin = getClass().getResourceAsStream("norelapi.drivers.default.json");
    if (null != rin) {
      addJsonDriverConfig(rin);
    }
    rin = getClass().getClassLoader().getResourceAsStream("norelapi.drivers.json");
    if (null != rin) {
      addJsonDriverConfig(rin);
    }
  }

  private void addJsonDriverConfig(InputStream in) {
    JsonReader jsonReader = Json.createReader(new InputStreamReader(in));
    JsonObject root = jsonReader.readObject();
    Set<String> driverNames = json.keySet();
    for (String name: driverNames) {
      JsonObject driverObj = root.getObject(name);
      String classname = driverObj.getString("classname");
      addDriver(name,classname);
    }

    jsonReader.close();
  }

  public void discoverConnectionInfoFromClasspath() {
    // look just for norelapi.connections.json at root, for now
    // makes no sense having a package related one
    InputStream rin = getClass().getClassLoader().getResourceAsStream("norelapi.connections.json");
    if (null != rin) {
      addJsonConnectionConfig(rin);
    }
  }

  private void addJsonConnectionConfig() {
    JsonReader jsonReader = Json.createReader(new InputStreamReader(in));
    JsonObject root = jsonReader.readObject();
    Set<String> connectionAliases = json.keySet();
    for (String alias: connectionAliases) {
      JsonObject connObj = root.getObject(alias);
      JsonString driverAlias = connObj.getString("driver");
      try {
        DriverInfo dInfo = getDriverInfo(driverAlias);
        JsonObject config = connObj.getObject("config");
        Set<String> configKeys = config.keySet();
        Hashtable<String,Object> props = new Hashtable<String,Object>();
        for (String key: configKeys) {
          ConfigurationPropertyInfo pInfo = dInfo.get(key);
          if (null == pInfo) {
            // warn
            System.out.println("No such property: '" + key + "' for driver: '" + dInfo.getName() + "' of connection: '" + alias + "'. Skipping.");
          } else {
            ConfigurationPropertyInfo.Type type = pInfo.getType();
            try {
              if (ConfigurationPropertyInfo.Type.String.equals(type)) {
                props.put(key,config.getString(key));
              } else if (ConfigurationPropertyInfo.Type.StringArray.equals(type)) {
                JsonArray array = config.getArray(key);
                String[] stringArray = new String[array.size()];
                array.toArray(stringArray);
                props.put(key,stringArray);
              } else if (ConfigurationPropertyInfo.Type.Long.equals(type)) {
                props.put(key,Long.parseLong(config.getString(key)));
              } else if (ConfigurationPropertyInfo.Type.Double.equals(type)) {
                props.put(key,Double.parseDouble(config.getString(key)));
              }
            } catch (ClassCastException cnfe) {
              cnfe.printStackTrace();
              System.out.println("Property key: '" + key + "' for driver: '" + dInfo.getName() + "' of connection: '" + alias + 
                "' was not of the expected type: '" + type + "'. Skipping.");
            }
          }
        }
        conenctionInfos.put(alias,new ConnectionInfo(alias,driverAlias,props));
      } catch (NoSuchEntityException nsee) {
        nsee.printStackTrace();
      }
    }

    jsonReader.close();
  }

  // CONNECTION DRIVER INFO LOADING METHODS
  /**
   * Loads the specified Driver and introspects it's available properties
   */
  public void addDriver(String name,String classname) throws DuplicateEntityException, NoSuchEntityException {
    if (driverInfos.contains(name)) {
      throw new DuplicateEntityException("Duplicate Driver named '" + name + "'");
    }
    try {
      Class dc = Class.forName(classname);
      Driver d = dc.newInstance();
      DriverInfo info = d.getInfo();
      DriverInfos.put(name,info);
      drivers.put(name,d);
    } catch (ClassNotFoundException cnfe) {
      throw new NoSuchEntityException("Class could not be found for driver '" + name + "'",name,cnfe);
    } catch (InstantiationException ie) {
      throw new NoSuchEntityException("Class could not be instantiated for driver '" + name + "'",name,ie);
    }
  }

  public void addDriver(String name,String classname,Collection<ConfigurationPropertyInfo> props) throws DuplicateEntityException, NoSuchEntityException {
    if (driverInfos.contains(name)) {
      throw new DuplicateEntityException("Duplicate Driver named '" + name + "'", name);
    }
    driverInfos.put(name,new DriverInfo(name,classname,props));
  }

  public void removeDriver(String name) throws NoSuchEntityException {
    if (!driverInfos.contains(name)) {
      throw new NoSuchEntityException("No Driver named '" + name + "'",name);
    }
    driverInfos.remove(name);
  }

  public DriverInfo getDriverInfo(String name) throws NoSuchEntityException {
    if (!driverInfos.contains(name)) {
      throw new NoSuchEntityException("No Driver named '" + name + "'",name);
    }
    return driverInfos.get(name);
  }

  private Driver getDriver(String name) throws NoSuchEntityException {
    if (!driverInfos.contains(name)) {
      throw new NoSuchEntityException("The Driver Info object with alias '" + name + "' does not exist",name);
    }
    if (drivers.containsKey(name)) {
      return drivers.get(name);
    }
    DriverInfo info = getDriverInfo(name);
    Class dc = Class.forName(info.getClassname());
    Driver d = dc.newInstance();
    drivers.put(name,d);
    return d;
  }

  // CONNECTION INSTANCE CONFIGURATION LOADING METHODS
  public void addConnectionInfo(String configAlias,String driverAlias,Hashtable<String,Object> configurationProperties) throws DuplicateEntityException {
    DriverInfo dInfo = getDriverInfo(driverAlias);
    ConnectionInfo info = new ConnectionInfo(configAlias,dInfo,configurationProperaties);
    connectionInfos.put(configAlias,info);
  }

  public void replaceConnectionInfo(String configAlias,String driverAlias,Hashtable<String,Object> configurationProperties) throws NoSuchEntityException, DuplicateEntityException {
    removeConnectionInfo(configAlias);
    addConnectionInfo(configAlias,driverAlias,configurationProperties);
  }

  public void removeConnectionInfo(String configAlias) throws NoSuchEntityException {
    if (!connectionInfos.contains(configAlias)) {
      throw new NoSuchEntityException("The Connection Info object with alias '" + configAlias + "' does not exist",configAlias);
    }
    connectionInfos.remove(configAlias);
  }

  // INSTANCE METHODS

  public Connection getConnection(String configAlias) throws ConnectionException, NoSuchEntityException {
    if (!connectionInfos.contains(configAlias)) {
      throw new NoSuchEntityException("The Connection Info object with alias '" + configAlias + "' does not exist",configAlias);
    }
    ConnectionInfo info = connectionInfos.get(configAlias);
    Driver driver = getDriver(info.getDriverInfo().getName());
    Connection conn = driver.createConnection(info.getConfiguration());
    connections.add(conn);
    return conn;
  }

  public Connection getConnection(String configAlias,String driverAlias,Hashtable<String,Object> configurationProperties) throws ConnectionException, DuplicateEntityException, NoSuchEntityException {
    addConnectionInfo(configAlias,driverAlias,configurationProperties);
    return getConnection(configAlias);
  }
}