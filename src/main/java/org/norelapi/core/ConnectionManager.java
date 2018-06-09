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
  private TreeSet<DriverInfo> driverInfos = new TreeSet<DriverInfo>();

  private Hashtable<String,Driver> drivers = new Hashtable<String,Driver>();

  private TreeSet<ConnectionInfo> connectionInfos = new TreeSet<ConnectionInfo>();

  private TreeSet<Connection> connections = new TreeSet<Connection>();

  public ConnectionManager() {
  }

  // CONNECTION DRIVER INFO LOADING METHODS
  public void addDriversFromClasspath() {

  }

  public void addDriver(String name,String classname,Collection<ConfigurationPropertyInfo> props) throws DuplicateEntityException {
    if (driverInfos.contains(name)) {
      throw new DuplicateEntityException("Duplicate Driver named '" + name + "'");
    }
    driverInfos.add(new DriverInfo(name,classname,props));
  }

  public void removeDriver(String name) {

  }

  public void getDriverInfo(String name) {

  }

  // CONNECTION INSTANCE CONFIGURATION LOADING METHODS

  public void addConnection(String configAlias,String driverClassname,Hashtable<String,Object> configurationProperties) throws DuplicateEntityException {

  }

  public void replaceConnection(String configAlias,String driverClassname,Hashtable<String,Object> configurationProperties) {
  }

  public void addConnectionsFromJson(String json) throws DuplicateEntityException, IOException {

  }

  public void addConnectionsFromJson(InputStream jsonInputStream) throws DuplicateEntityException, IOException {

  }

  public void addConnectionsFromJson(File jsonFile) throws DuplicateEntityException, IOException {

  }

  public void saveConnectionsToJson(OutputStream jsonOutputStream) throws IOException {

  }

  public void saveConnectionsToJson(File jsonFile) throws IOException {
    FileOutputStream fos = new FileOutputStream(jsonFile);
    saveConnectionsToJson(fos);
    fos.flush();
    fos.close();
  }

  public void addConnectionsFromClasspath() throws DuplicateEntityException, IOException {

  }

  // INSTANCE METHODS

  public Connection getConnection(String configAlias) throws ConnectionException, NoSuchEntityException {
    ConnectionInfo info = connectionInfos.get(configAlias); // TODO implement this efficiently
    
  }

  public Connection getConnection(String configAlias,String driverClassname,Hashtable<String,Object> configurationProperaties) throws ConnectionException, DuplicateEntityException, NoSuchEntityException {
    addConnection(configAlias,driverClassname,configurationProperaties);
    return getConnection(configAlias);
  }
}