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
 * A special type of operation exception where the operation could not proceed
 * because the underlying object was in an invalid state at the commencement
 * of the operation. E.g. the connection was closed, or catalog unavailable.
 */
public class InvalidStateException extends OperationException {
  private String requiredStateDescription;
  private String encounteredStateDescription;
  public InvalidStateException(String message,String requiredStateDescription,String encounteredStateDescription) {
    super(message);
    this.requiredStateDescription = requiredStateDescription;
    this.encounteredStateDescription = encounteredStateDescription;
  }
  public InvalidStateException(String message,String requiredStateDescription,String encounteredStateDescription,Exception chain) {
    super(message,chain);
    this.requiredStateDescription = requiredStateDescription;
    this.encounteredStateDescription = encounteredStateDescription;
  }
  public String getRequiredState() {
    return requiredStateDescription;
  }
  public String getEncounteredState() {
    return encounteredStateDescription;
  }
}