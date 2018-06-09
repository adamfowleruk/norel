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

/**
 * MongoDB implementation of the NoREL API Driver interface.
 * 
 * Must always load, and so has no run time class dependencies within the MongoDB package.
 */
public class MongoDBDriver implements Driver {
  private static final String NAME = "MongoDB";
  private static final String CLASSNAME = "org.norelapi.impl.mongodb.MongoDBConnection"; 

  public DriverInfo getInfo() {
    ArrayList<ConfigurationPropertyInfo> props = new ArrayList<ConfigurationPropertyInfo>();
    props.add(new ConfigurationPropertyInfo("hosts",String[].class,"Single host, or multiple hostnames, of MongoDB Servers"));
    props.add(new ConfigurationPropertyInfo("username",String.class,"Database access username"));
    props.add(new ConfigurationPropertyInfo("password",String.class,"Database access password"));
    DriverInfo info = new DriverInfo(NAME,CLASSNAME,props);
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
    }
  }
}