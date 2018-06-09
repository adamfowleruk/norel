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

/**
 * Represents the configuration for a named connection instance
 */
public class ConnectionInfo {
  private String alias;
  private DriverInfo driverInfo;
  private Hashtable<String,Object> configuration;
  
  public ConnectionInfo(String alias,DriverInfo driverInfo,Hashtable<String,Object> config) {
    this.alias = alias;
    this.driverInfo = driverInfo;
    this.configuration = config;
  }

  public String getAlias() {
    return alias;
  }

  public DriverInfo getDriverInfo() {
    return driverInfo;
  }

  public Hashtable<String,Object> getConfiguration() {
    return configuration;
  }
}