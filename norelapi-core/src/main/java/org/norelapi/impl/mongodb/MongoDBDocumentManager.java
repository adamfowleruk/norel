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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.types.ObjectId;
import com.mongodb.MongoClientSettings;
import com.mongodb.ConnectionString;
import com.mongodb.ServerAddress;
import com.mongodb.MongoCredential;
import com.mongodb.client.FindIterable;
import org.bson.Document;

import org.norelapi.impl.shared.IOUtils;
import org.norelapi.impl.shared.JsonDocument;
import org.norelapi.core.Result;
import org.norelapi.core.UnsupportedOperationException;
import org.norelapi.core.DocumentManager;
import org.norelapi.core.SingleOperationResult;
import org.norelapi.core.BulkOperationResult;
import org.norelapi.core.OperationException;
import org.norelapi.impl.shared.GenericBulkOperationResult;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Collection;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * MongoDB implementation of the NoREL API DocumentManager interface
 */
public class MongoDBDocumentManager implements DocumentManager {
  private static final Logger logger = LogManager.getLogger();
  private MongoDBLibrary library;

  protected MongoDBDocumentManager(MongoDBLibrary library) {
    this.library = library;
  }

  // Simple, required, operations

  public SingleOperationResult createDocument(String id,InputStream docSrc) throws OperationException {
    try {
      // create document and add to MongoDB
      Document document = Document.parse(IOUtils.readInputStreamIntoString(docSrc));
      if (null != id) {
        document.putIfAbsent("_id",id);
      }
      library.getCollection().insertOne(document);
      return new SingleOperationResult<Result>(true,"Successfully created Document with _id: '" + id + "'",new Result()); // TODO should this be a jsondocument with an ID???
    } catch (Exception e) {
      return new SingleOperationResult<Result>(false,"Error executing MongoDB createDocument",e);
    }
  }

  public SingleOperationResult createDocument(String id,InputStream docSrc,Collection<String> collections) throws OperationException {
    collectionCheck(collections,"createDocument(id,docSrc,collections)");
    return createDocument(id,docSrc);
  }

  private void collectionCheck(Collection<String> collections,String func) throws OperationException {
    if (collections.size() > 1) {
      throw new UnsupportedOperationException(
        "MongoDB only supports inserting each document in to 1 Collection",func
      );
    }
    String libCol = library.getAlias();
    if (collections.size() == 1 && !libCol.equals(collections.iterator().next())) {
      throw new UnsupportedOperationException(
        "MongoDB only supports inserting each document in to the Collection of it's NoREL API Library Object: '" + libCol + "'",
        func
      );
    }
  }

  public SingleOperationResult updateDocument(String id,InputStream docSrc) throws OperationException {
    try {
      Document document = Document.parse(IOUtils.readInputStreamIntoString(docSrc));
      MongoCollection<Document> col = library.getCollection();
      col.replaceOne(Filters.eq("_id", new ObjectId(id)),document);
      return new SingleOperationResult<Result>(true,"Successfully updated document with _id: '" + id + "'",new Result());
    } catch (Exception e) {
      return new SingleOperationResult<Result>(false,"Error executing MongoDB updateDocument",e);
    }
  }

  public SingleOperationResult updateDocument(String id,InputStream docSrc,Collection<String> collections) throws OperationException {
    collectionCheck(collections,"updateDocument(id,docSrc,collections)");
    return updateDocument(id,docSrc);
  }

  public SingleOperationResult deleteDocument(String id) throws OperationException {
    try {
      logger.trace("_id: {}",id);
      library.getCollection().deleteOne(Filters.eq("_id", new ObjectId(id)));
      return new SingleOperationResult<Result>(true,"Successfully deleted document with _id: '" + id + "'",new Result());
    } catch (Exception e) {
      return new SingleOperationResult<Result>(false,"Error executing MongoDB deleteDocument",e);
    }
  }

  public SingleOperationResult getDocument(String id) throws OperationException {
    try {
      Document document = library.getCollection().find(Filters.eq("_id", new ObjectId(id))).first();
      return new SingleOperationResult<JsonDocument>(true,
        "Successfully retreived document with _id: '" + id + "'",new JsonDocument(document.toJson(),id));
    } catch (Exception e) {
      return new SingleOperationResult<Result>(false,"Error executing MongoDB getDocument",e);
    }
  }
  
  public BulkOperationResult listDocuments(String collection) throws OperationException {
    try {
      String libCol = library.getAlias();
      if (!libCol.equals(collection)) {
        throw new UnsupportedOperationException(
          "MongoDB only supports listings document within the Collection of it's NoREL API Library Object: '" + libCol + "'",
          "listDocuments(collection)"
        );
      }
      FindIterable<Document> iter = library.getCollection().find();
      return new GenericBulkOperationResult(true,"Successfully listed documents of collection: '" + collection + "'",
        new MongoDBFindIterator<SingleOperationResult<Result>>(iter));
    } catch (Exception e) {
      return new GenericBulkOperationResult(false,"Error executing MongoDB listDocuments",e,null);
    }
  }

  public boolean hasIndex(String indexAlias) throws OperationException {
    try {
      MongoCursor<Document> it = library.getCollection().listIndexes().iterator();
      while ( it.hasNext() ) {
        Document index = it.next();
        if (indexAlias.equals(index.get( "name" ).toString())) {
          return true;
        }
      }
      return false;
    } catch (Exception e) {
      throw new OperationException("Error executing hasIndex on MongoDB",e);
    }
  }

  public BulkOperationResult findDocuments(String indexAlias,String indexValue) throws OperationException {
    try {
      FindIterable<Document> iter = library.getCollection().find(Filters.eq(indexAlias,indexValue));
      return new GenericBulkOperationResult(true,"Found documents",new MongoDBFindIterator<SingleOperationResult<Result>>(iter));
    } catch (Exception e) {
      return new GenericBulkOperationResult(false,"Error executing MongoDB findDocuments",e,null);
    }
  }

  // TODO complex, optional, operations
}