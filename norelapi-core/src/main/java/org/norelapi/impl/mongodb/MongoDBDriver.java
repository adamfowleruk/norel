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
package org.norelapi.impl.mongodb;

import org.norelapi.core.Connection;
import org.norelapi.core.Driver;
import org.norelapi.core.DriverInfo;
import org.norelapi.core.OperationException;
import org.norelapi.core.ConfigurationPropertyInfo;

import java.util.Hashtable;
import java.util.ArrayList;

/**
 * MongoDB implementation of the NoREL API Driver interface.
 * 
 * Must always load, and so has no run time class dependencies within the MongoDB package.
 */
public class MongoDBDriver implements Driver {
  private static final String NAME = "mongodb";
  private static final String CLASSNAME = "org.norelapi.impl.mongodb.MongoDBConnection"; 

  public static final String CONNECTION_STRING = "connectionString"; 
  public static final String USERNAME = "username"; 
  public static final String PASSWORD = "password"; 
  public static final String DATABASE = "database"; 
  public static final String SSL_ENABLED = "sslEnabled"; 

  /**
   * Default constructor for classloading
   */
  public MongoDBDriver() {}

  public DriverInfo getInfo() {
    ArrayList<ConfigurationPropertyInfo> props = new ArrayList<ConfigurationPropertyInfo>();
    props.add(new ConfigurationPropertyInfo(CONNECTION_STRING,ConfigurationPropertyInfo.Type.String,"Single host, or multiple hostnames, of MongoDB Servers, using the mongodb:// URL specification."));
    props.add(new ConfigurationPropertyInfo(SSL_ENABLED,ConfigurationPropertyInfo.Type.Boolean,"Is ssl Enabled? Default: 'true'"));
    props.add(new ConfigurationPropertyInfo(USERNAME,ConfigurationPropertyInfo.Type.String,"Database access username"));
    props.add(new ConfigurationPropertyInfo(PASSWORD,ConfigurationPropertyInfo.Type.String,"Database access password"));
    props.add(new ConfigurationPropertyInfo(DATABASE,ConfigurationPropertyInfo.Type.String,"Database name you are associating with the credentials"));
    DriverInfo info = new DriverInfo(NAME,CLASSNAME,props);
    return info;
  }

  // Internal methods
  public Connection createConnection(Hashtable<String,Object> config) throws OperationException {
    try {
      Class mongoC = Class.forName(CLASSNAME);
      Connection conn = (Connection)mongoC.newInstance(); // default constructor required to try to find all dependent classes
      conn.configure(config);
      return conn;
    } catch (ClassNotFoundException cnfe) {
      throw new OperationException("Could not find required class for MongoDB Connection - Do you have the MongoDB Java API JAR files included?",cnfe);
    } catch (InstantiationException ie) {
      throw new OperationException("Could not instantiate required class for MongoDB Connection",ie);
    } catch (IllegalAccessException iae) {
      throw new OperationException("Could not instantiate required class for MongoDB Connection",iae);
    }
  }
}