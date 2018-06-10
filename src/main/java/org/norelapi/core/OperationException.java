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
 * Generic high level exception for all operations in the NoREL API.
 * Only typically thrown if the operation could not be issued, rather than the 
 * result of the operation throwing an exception.
 *
 * The OperationResult class can also contain an exception from the remote server.
 *
 * If this exception is thrown it generally means that the request could not
 * be issued to the underlying database server for some reason.
 */
public class OperationException extends Exception {
  public OperationException(String message) {
    super(message);
  }
  public OperationException(String message,Exception chain) {
    super(message,chain);
  }
}