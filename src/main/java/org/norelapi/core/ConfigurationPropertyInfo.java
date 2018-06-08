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

/**
 * Holds a description of basic configuration (String name, Object value pairs)
 */
public class ConfigurationPropertyInfo {
  private String name;
  private Class type;
  private String description;

  public ConfigurationPropertyInfo(String name,Class type,String description) {
    this.name = name;
    this.type = type;
    this.description = description;
  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }

  public String description() {
    return description;
  }

  public boolean equals(String name) {
    return this.name.equals(name);
  }

  public boolean equals(ConfigurtionPropertyInfo info) {
    return this.name.equals(info.name);
  }
}