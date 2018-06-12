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
package org.norelapi.impl.shared;

import javax.json.Json;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;
import javax.json.JsonString;
import javax.json.JsonObject;
import javax.json.JsonArray;

import org.norelapi.core.Document;

import java.io.StringReader;

/**
 * Implements a JSON Document using the javax.json library.
 */
public class JsonDocument extends Document {
  private JsonObject json = null;

  public JsonDocument(String json) {
    super(json);
  }

  public JsonDocument(String json,String id) {
    super(json,id);
  }

  public JsonDocument(JsonObject json) {
    this.json = json;
  }

  public JsonDocument(JsonObject json,String id) {
    super(null,id);
    this.json = json;
  }

  public JsonObject getJson() {
    if (null == json) {
      if (null == payload) {
        return null;
      } else {
        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject obj = reader.readObject();
        reader.close();
        return obj;
      }
    } else {
      return json;
    }
  }

  public String getResult() {
    if (null == json) {
      return payload;
    } else {
      return json.toString();
    }
  }
}