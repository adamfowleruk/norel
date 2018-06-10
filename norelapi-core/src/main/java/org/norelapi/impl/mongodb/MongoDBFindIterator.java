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
package org.norelapi.impl.mongodb;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.FindIterable;
import org.bson.Document;

import org.norelapi.core.SingleOperationResult;
import org.norelapi.impl.shared.GenericSingleOperationResult;

import java.util.Iterator;

/**
 * A thin NoREL API wrapper around MongoDB's iterator
 */
public class MongoDBFindIterator<SingleOperationResult> implements Iterator<SingleOperationResult> {
  private FindIterable<Document> srcIter;
  private MongoCursor<Document> iterator;
  
  public MongoDBFindIterator(FindIterable<Document> srcIter) {
    this.srcIter = srcIter;
    iterator = srcIter.iterator();
  }
  
  public boolean hasNext() {
    return iterator.hasNext();
  }

  public SingleOperationResult next() {
    Document nextDoc = iterator.next();
    return (SingleOperationResult)new GenericSingleOperationResult(true,"Successfully loaded document",nextDoc.toJson());
  }

  public void remove() {
    throw new java.lang.UnsupportedOperationException();
  }
  
}