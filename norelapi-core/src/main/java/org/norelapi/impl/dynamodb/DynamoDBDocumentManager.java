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
package org.norelapi.impl.dynamodb;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DynamoDBDocumentManager implements DocumentManager {
  private static final Logger logger = LogManager.getLogger();
  
  // Simple, required, operations

  public SingleOperationResult createDocument(String id,InputStream docSrc) throws OperationException {

  }
  public SingleOperationResult createDocument(String id,InputStream docSrc,Collection<String> collections) throws OperationException {

  }
  public SingleOperationResult updateDocument(String id,InputStream docSrc) throws OperationException {

  }
  public SingleOperationResult updateDocument(String id,InputStream docSrc,Collection<String> collections) throws OperationException {

  }
  public SingleOperationResult deleteDocument(String id) throws OperationException {

  }
  public SingleOperationResult getDocument(String id) throws OperationException {

  }
  
  public BulkOperationResult listDocuments(String collection) throws OperationException {

  }

  public boolean hasIndex(String indexAlias) throws OperationException {

  }
  public BulkOperationResult findDocuments(String indexAlias,String indexValue) throws OperationException {

  }

  // TODO complex, optional, operations
}