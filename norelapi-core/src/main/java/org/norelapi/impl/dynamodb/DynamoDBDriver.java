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
package org.norelapi.impl.dynamodb;

import org.norelapi.core.Connection;
import org.norelapi.core.Driver;
import org.norelapi.core.DriverInfo;
import org.norelapi.core.OperationException;
import org.norelapi.core.ConfigurationPropertyInfo;

import java.util.Hashtable;
import java.util.ArrayList;

/**
 * AWS DynamoDB implementation of the NoREL API Driver interface.
 * 
 * Must always load, and so has no run time class dependencies within the DynamoDB package.
 */
public class DynamoDBDriver implements Driver {
  private static final Logger logger = LogManager.getLogger();
  private static final String NAME = "dynamodb";
  private static final String CLASSNAME = "org.norelapi.impl.dynamodb.DynamoDBConnection"; 

  public static final String CONNECTION_STRING = "connectionString"; 
  public static final String USERNAME = "username"; 
  public static final String PASSWORD = "password"; 
  public static final String REGION = "region"; 

  /**
   * Default constructor for classloading
   */
  public DynamoDBDBDriver() {}

  public DriverInfo getInfo() {
    ArrayList<ConfigurationPropertyInfo> props = new ArrayList<ConfigurationPropertyInfo>();
    props.add(new ConfigurationPropertyInfo(CONNECTION_STRING,ConfigurationPropertyInfo.Type.String,"AWS host url. E.g. http://localhost:8000"));
    props.add(new ConfigurationPropertyInfo(REGION,ConfigurationPropertyInfo.Type.String,"The AWS Region. E.g. us-west-1"));
    props.add(new ConfigurationPropertyInfo(USERNAME,ConfigurationPropertyInfo.Type.String,"Database access username"));
    props.add(new ConfigurationPropertyInfo(PASSWORD,ConfigurationPropertyInfo.Type.String,"Database access password"));
    DriverInfo info = new DriverInfo(NAME,CLASSNAME,props);
    return info;
  }

  // Internal methods
  public Connection createConnection(Hashtable<String,Object> config) throws OperationException {
    try {
      Class dc = Class.forName(CLASSNAME);
      Connection conn = (Connection)dc.newInstance(); // default constructor required to try to find all dependent classes
      conn.configure(config);
      return conn;
    } catch (ClassNotFoundException cnfe) {
      throw new OperationException("Could not find required class for DynamoDB Connection - Do you have the DynamoDB Java API JAR files included?",cnfe);
    } catch (InstantiationException ie) {
      throw new OperationException("Could not instantiate required class for DynamoDB Connection",ie);
    } catch (IllegalAccessException iae) {
      throw new OperationException("Could not instantiate required class for DynamoDB Connection",iae);
    }
  }
}