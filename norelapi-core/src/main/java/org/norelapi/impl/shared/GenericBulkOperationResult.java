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

import org.norelapi.core.SingleOperationResult;
import org.norelapi.core.BulkOperationResult;

import java.util.Iterator;

/**
 * A generic implementation of a bulk iterator that takes a database's own implementation
 * of a single iterator as it's content.
 */
public class GenericBulkOperationResult extends BulkOperationResult {
  private Iterator<SingleOperationResult> iterator;

  public GenericBulkOperationResult(boolean success,String message,Exception exc,Iterator<SingleOperationResult> iter) {
    super(success,message,exc);
    iterator = iter;
  }
  
  public GenericBulkOperationResult(boolean success,String message,Iterator<SingleOperationResult> iter) {
    super(success,message);
    iterator = iter;
  }

  public Iterator<SingleOperationResult> getResultIterator() {
    return iterator;
  }
}