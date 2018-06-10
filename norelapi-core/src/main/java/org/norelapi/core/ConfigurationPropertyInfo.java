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
 * Holds a description of basic configuration (String name, Object value pairs)
 */
public class ConfigurationPropertyInfo {
  public enum Type {
    String("string"),StringArray("string[]"),Long("long"),Double("double"),
    Boolean("boolean");

    private final String text;

    Type(String text) {
      this.text = text;
    }

    public String toString() {
      return text;
    }
  }
  private String name;
  private Type type;
  private String description;

  public ConfigurationPropertyInfo(String name,Type type,String description) {
    this.name = name;
    this.type = type;
    this.description = description;
  }

  public String getName() { 
    return name; 
  }

  public Type getType() {
    return type;
  }

  public String description() {
    return description;
  }

  public boolean equals(String name) {
    return this.name.equals(name);
  }

  public boolean equals(ConfigurationPropertyInfo info) {
    return this.name.equals(info.name);
  }

  public static Type getType(String typeString) {
    if (Type.String.toString().equals(typeString)) {
      return Type.String;
    }
    if (Type.StringArray.toString().equals(typeString)) {
      return Type.StringArray;
    }
    if (Type.Boolean.toString().equals(typeString)) {
      return Type.Boolean;
    }
    if (Type.Long.toString().equals(typeString)) {
      return Type.Long;
    }
    if (Type.Double.toString().equals(typeString)) {
      return Type.Double;
    }
    return null;
  }
}