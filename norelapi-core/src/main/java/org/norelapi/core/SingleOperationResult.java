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

import java.io.InputStream;

/**
 * Represents the result of a single operation. E.g. Document add
 *
 * Abstract because the underlying database may have its own primitives to handle
 * retrieving the response reason, necessitating this abstraction.
 *
 * Doing otherwise could lead to poor performance. E.g. if getting the full
 * response required another rountrip to the database server, or a lot of parsing.
 */
public abstract class SingleOperationResult extends OperationResult {
  public SingleOperationResult(boolean success,String message,Exception exc) {
    super(success,message,exc);
  }
  public SingleOperationResult(boolean success,String message) {
    super(success,message);
  }
  public abstract String getResponseAsString() throws InvalidStateException;
  public abstract InputStream getResponseAsStream() throws InvalidStateException;
}