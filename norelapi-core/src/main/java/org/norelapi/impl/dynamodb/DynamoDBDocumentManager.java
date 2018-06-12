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

import org.norelapi.impl.shared.IOUtils;
import org.norelapi.core.UnsupportedOperationException;
import org.norelapi.core.DocumentManager;
import org.norelapi.core.SingleOperationResult;
import org.norelapi.core.BulkOperationResult;
import org.norelapi.core.OperationException;
import org.norelapi.core.InvalidStateException;
import org.norelapi.core.Library;
import org.norelapi.core.LibraryInfo;
import org.norelapi.core.NoSuchEntityException;
import org.norelapi.impl.shared.GenericBulkOperationResult;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Collection;
import java.util.Scanner;
import java.util.Hashtable;
import java.io.BufferedReader;
import java.io.InputStreamReader;


public class DynamoDBDocumentManager implements DocumentManager {
  private static final Logger logger = LogManager.getLogger();

  DynamoDBLibrary library;

  public DynamoDBDocumentManager(DynamoDBLibrary library) {
    this.library = library;
  }

  // Simple, required, operations

  public SingleOperationResult createDocument(String id,InputStream docSrc) throws OperationException {
    return null;
  }
  public SingleOperationResult createDocument(String id,InputStream docSrc,Collection<String> collections) throws OperationException {
    return null;
  }
  public SingleOperationResult updateDocument(String id,InputStream docSrc) throws OperationException {
    return null;
  }
  public SingleOperationResult updateDocument(String id,InputStream docSrc,Collection<String> collections) throws OperationException {
    return null;
  }
  public SingleOperationResult deleteDocument(String id) throws OperationException {
    return null;
  }
  public SingleOperationResult getDocument(String id) throws OperationException {
    return null;
  }
  
  public BulkOperationResult listDocuments(String collection) throws OperationException {
    return null;
  }

  public boolean hasIndex(String indexAlias) throws OperationException {
    return false;
  }
  public BulkOperationResult findDocuments(String indexAlias,String indexValue) throws OperationException {
    return null;
  }

  // TODO complex, optional, operations
  protected DynamoDBLibrary getLibrary() {
    return library;
  }
}