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
 * A special kind of Operation Exception where the specified entity alias does
 * not exist, and so the operation cannot proceed.
 */
public class NoSuchEntityException extends OperationException {
  private String entityAlias;

  public NoSuchEntityException(String message, String entityAlias) {
    super(message);
    this.entityAlias = entityAlias;
  }

  public String getEntityAlias() {
    return entityAlias;
  }
}