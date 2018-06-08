/*! ******************************************************************************
 *
 * NoREL API
 *
 * Copyright (C) 018 by Adam Fowler : http://www.adamfowler.org/
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

import java.util.Collection;
import java.util.ArrayList;

/**
 * Represents the high level information about a NoSQL database connection driver, and its allowed configuration
 */
public class DriverInfo {
  private String driverAlias;
  private String driverClassname;
  private ArrayList<ConfigurationPropertyInfo> configurationProperties = new ArrayList<ConfigurationPropertyInfo>();

  public DriverInfo(String driverAlias,String driverClassname,Collection<ConfigurationPropertyInfo> configurationProperties) {
    this.driverAlias = driverAlias;
    this.driverClassname = driverClassname;
    this.configurationProperties.addAll(configurationProperties);
  }

  public String getAlias() {
    return driverAlias;
  }

  public String getClassname() {
    return driverClassname;
  }
  
  public Collection<ConfigurationPropertyInfo> getConfigurationProperties() {
    return configurationProperties;
  }
}