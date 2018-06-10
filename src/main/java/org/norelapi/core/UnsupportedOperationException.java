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
 * A special kind of operation exception thrown by a driver to specify
 * that an operation, or a particular calling configuration of it 
 * (E.g. use of multiple collections in a DB that don't support them),
 * is not supported.
 */
public class UnsupportedOperationException extends OperationException {
  private String operation;

  public UnsupportedOperationException(String message,String operation) {
    super(message);
    this.operation = operation;
  }
  public UnsupportedOperationException(String message,String operation,Exception chain) {
    super(message,chain);
    this.operation = operation;
  }
  public String getOperation() {
    return operation;
  }
}