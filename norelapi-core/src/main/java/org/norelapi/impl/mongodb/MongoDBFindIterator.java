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
import org.norelapi.core.Result;
import org.norelapi.impl.shared.JsonDocument;

import java.util.Iterator;

/**
 * A thin NoREL API wrapper around MongoDB's iterator
 */
public class MongoDBFindIterator<T extends SingleOperationResult> implements Iterator<T> {
  private FindIterable<Document> srcIter;
  private MongoCursor<Document> iterator;
  
  public MongoDBFindIterator(FindIterable<Document> srcIter) {
    this.srcIter = srcIter;
    iterator = srcIter.iterator();

  }
  
  public boolean hasNext() {
    return iterator.hasNext();
  }

  public T next() {
    Document nextDoc = iterator.next();
    return (T) new SingleOperationResult<JsonDocument>(true,"Successfully loaded document",
      new JsonDocument(nextDoc.toJson(),nextDoc.get("_id").toString())
    );
  }

  public void remove() {
    throw new java.lang.UnsupportedOperationException();
  }
  
}