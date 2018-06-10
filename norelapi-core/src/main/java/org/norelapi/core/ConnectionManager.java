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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Hashtable;
import java.util.TreeSet;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

import java.util.Set;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonObject;
import javax.json.JsonArray;

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
  private static final Logger logger = LogManager.getLogger();
  private Hashtable<String,DriverInfo> driverInfos = new Hashtable<String,DriverInfo>();

  private Hashtable<String,Driver> drivers = new Hashtable<String,Driver>();

  private Hashtable<String,ConnectionInfo> connectionInfos = new Hashtable<String,ConnectionInfo>();

  private ArrayList<Connection> connections = new ArrayList<Connection>();

  public ConnectionManager() throws DuplicateEntityException, NoSuchEntityException {
    discoverDriversFromClasspath();
    discoverConnectionInfoFromClasspath();
  }

  public void removeAllDriverInfos() {
    driverInfos.clear();
    // TODO security/auditability - do not remove any in use
  }

  public Collection<DriverInfo> listDriverInfos() {
    ArrayList<DriverInfo> dList = new ArrayList<DriverInfo>();
    for (Map.Entry<String,Driver> driverEntry: drivers.entrySet()) {
      dList.add(driverEntry.getValue().getInfo());
    }
    return dList;
  }

  public void removeAllConnectionInfos() {
    connectionInfos.clear();
    // TODO security/auditability - do not remove any in use
  }

  public void discoverDriversFromClasspath() throws DuplicateEntityException, NoSuchEntityException {
    // look just for a norelapi.drivers.default.json within the package, and norelapi.drivers.json at root, for now
    InputStream rin = getClass().getResourceAsStream("norelapi.drivers.default.json");
    if (null != rin) {
      addJsonDriverConfig(rin);
    } else {
      logger.trace("FATAL: Could not find default NoREL API Driver library in JAR file!");
    }
    rin = getClass().getClassLoader().getResourceAsStream("norelapi.drivers.json");
    if (null != rin) {
      addJsonDriverConfig(rin);
    }
  }

  private void addJsonDriverConfig(InputStream in) throws DuplicateEntityException, NoSuchEntityException {
    JsonReader jsonReader = Json.createReader(new InputStreamReader(in));
    JsonObject root = jsonReader.readObject();
    Set<String> driverNames = root.keySet();
    for (String name: driverNames) {
      JsonObject driverObj = root.getJsonObject(name);
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

  private void addJsonConnectionConfig(InputStream in) {
    JsonReader jsonReader = Json.createReader(new InputStreamReader(in));
    JsonObject root = jsonReader.readObject();
    Set<String> connectionAliases = root.keySet();
    for (String alias: connectionAliases) {
      JsonObject connObj = root.getJsonObject(alias);
      String driverAlias = connObj.getString("driver");
      try {
        DriverInfo dInfo = getDriverInfo(driverAlias);
        JsonObject config = connObj.getJsonObject("config");
        Set<String> configKeys = config.keySet();
        Hashtable<String,Object> props = new Hashtable<String,Object>();
        for (String key: configKeys) {
          ConfigurationPropertyInfo pInfo = dInfo.getProperty(key);
          if (null == pInfo) {
            // warn
            logger.trace("No such property: '{}' for driver: '{}' of connection: '{}'. Skipping.",key,dInfo.getAlias(),alias);
          } else {
            ConfigurationPropertyInfo.Type type = pInfo.getType();
            try {
              if (ConfigurationPropertyInfo.Type.String.equals(type)) {
                props.put(key,config.getString(key));
              } else if (ConfigurationPropertyInfo.Type.StringArray.equals(type)) {
                JsonArray array = config.getJsonArray(key);
                String[] stringArray = new String[array.size()];
                array.toArray(stringArray);
                props.put(key,stringArray);
              } else if (ConfigurationPropertyInfo.Type.Long.equals(type)) {
                props.put(key,Long.parseLong(config.getString(key)));
              } else if (ConfigurationPropertyInfo.Type.Double.equals(type)) {
                props.put(key,Double.parseDouble(config.getString(key)));
              } else if (ConfigurationPropertyInfo.Type.Boolean.equals(type)) {
                props.put(key,config.getBoolean(key));
              }
            } catch (ClassCastException cnfe) {
              cnfe.printStackTrace();
              logger.trace("Property key: '{}' for driver: '{}' of connection: '{}' was not of the expected type: '{}'. Skipping.",
                key,dInfo.getAlias(),alias,type);
            }
          }
        }
        connectionInfos.put(alias,new ConnectionInfo(alias,dInfo,props));
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
    if (driverInfos.containsKey(name)) {
      throw new DuplicateEntityException("Duplicate Driver named '" + name + "'",name);
    }
    try {
      Class dc = Class.forName(classname);
      Driver d = (Driver)dc.newInstance();
      DriverInfo info = d.getInfo();
      driverInfos.put(name,info);
      drivers.put(name,d);
      logger.trace("Added driver with alias: '{}'", name);
    } catch (ClassNotFoundException cnfe) {
      throw new NoSuchEntityException("Class could not be found for driver '" + name + "'",name,cnfe);
    } catch (InstantiationException ie) {
      throw new NoSuchEntityException("Class could not be instantiated for driver '" + name + "'",name,ie);
    } catch (IllegalAccessException iae) {
      throw new NoSuchEntityException("Class could not be instantiated for driver '" + name + "'",name,iae);
    }
  }

  public void addDriver(String name,String classname,Collection<ConfigurationPropertyInfo> props) throws DuplicateEntityException, NoSuchEntityException {
    if (driverInfos.containsKey(name)) {
      throw new DuplicateEntityException("Duplicate Driver named '" + name + "'", name);
    }
    driverInfos.put(name,new DriverInfo(name,classname,props));
  }

  public void removeDriver(String name) throws NoSuchEntityException {
    if (!driverInfos.containsKey(name)) {
      throw new NoSuchEntityException("No Driver named '" + name + "'",name);
    }
    driverInfos.remove(name);
  }

  public DriverInfo getDriverInfo(String name) throws NoSuchEntityException {
    if (!driverInfos.containsKey(name)) {
      throw new NoSuchEntityException("No Driver named '" + name + "'",name);
    }
    return driverInfos.get(name);
  }

  private Driver getDriver(String name) throws NoSuchEntityException {
    if (!driverInfos.containsKey(name)) {
      throw new NoSuchEntityException("The Driver Info object with alias '" + name + "' does not exist",name);
    }
    if (drivers.containsKey(name)) {
      return drivers.get(name);
    }
    DriverInfo info = getDriverInfo(name);
    try {
      Class dc = Class.forName(info.getClassname());
      Driver d = (Driver)dc.newInstance();
      drivers.put(name,d);
      return d;
    } catch (ClassNotFoundException cnfe) {
      throw new NoSuchEntityException("Class could not be found for driver '" + name + "'",name,cnfe);
    } catch (InstantiationException ie) {
      throw new NoSuchEntityException("Class could not be instantiated for driver '" + name + "'",name,ie);
    } catch (IllegalAccessException iae) {
      throw new NoSuchEntityException("Class could not be instantiated for driver '" + name + "'",name,iae);
    }
  }

  // CONNECTION INSTANCE CONFIGURATION LOADING METHODS
  public void addConnectionInfo(String configAlias,String driverAlias,Hashtable<String,Object> configurationProperties) throws NoSuchEntityException, DuplicateEntityException {
    DriverInfo dInfo = getDriverInfo(driverAlias);
    ConnectionInfo info = new ConnectionInfo(configAlias,dInfo,configurationProperties);
    connectionInfos.put(configAlias,info);
  }

  public void replaceConnectionInfo(String configAlias,String driverAlias,Hashtable<String,Object> configurationProperties) throws NoSuchEntityException, DuplicateEntityException {
    removeConnectionInfo(configAlias);
    addConnectionInfo(configAlias,driverAlias,configurationProperties);
  }

  public void removeConnectionInfo(String configAlias) throws NoSuchEntityException {
    if (!connectionInfos.containsKey(configAlias)) {
      throw new NoSuchEntityException("The Connection Info object with alias '" + configAlias + "' does not exist",configAlias);
    }
    connectionInfos.remove(configAlias);
  }

  // INSTANCE METHODS

  public Connection getConnection(String configAlias) throws ConnectionException, NoSuchEntityException, OperationException {
    if (!connectionInfos.containsKey(configAlias)) {
      throw new NoSuchEntityException("The Connection Info object with alias '" + configAlias + "' does not exist",configAlias);
    }
    ConnectionInfo info = connectionInfos.get(configAlias);
    Driver driver = getDriver(info.getDriverInfo().getAlias());
    Connection conn = driver.createConnection(info.getConfiguration());
    connections.add(conn);
    return conn;
  }

  public Connection getConnection(String configAlias,String driverAlias,Hashtable<String,Object> configurationProperties) throws ConnectionException, DuplicateEntityException, NoSuchEntityException, OperationException {
    addConnectionInfo(configAlias,driverAlias,configurationProperties);
    return getConnection(configAlias);
  }
}