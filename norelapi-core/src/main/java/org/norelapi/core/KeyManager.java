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

import java.util.Collection;
import java.io.InputStream;
import java.io.Serializable;

/**
 * Represents a manager class to perform Key-Value operations on the underlying 
 * Library.
 */
public interface KeyManager {
  // Simple, required, operations

  public SingleOperationResult putKey(String key,InputStream value) throws OperationException;
  public SingleOperationResult putKey(String key,String value) throws OperationException;
  public SingleOperationResult putKey(String key,Serializable value) throws OperationException;

  public SingleOperationResult getKey(String key) throws OperationException;

  public SingleOperationResult deleteKey(String key) throws OperationException;

  // TODO complex, optional, operations
}