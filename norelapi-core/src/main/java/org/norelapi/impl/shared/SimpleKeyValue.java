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

import org.norelapi.core.KeyValue;

/**
 * Implements a basic wrapper for a KeyValue store item
 */
public class SimpleKeyValue<KT extends String,VT extends Object> extends KeyValue {
  VT value = null;

  public SimpleKeyValue(KT key,VT value) {
    super(value.toString(),key);
    this.value = value;
  }

  public VT getValue() {
    return value;
  }
}