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
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.types.ObjectId;
import com.mongodb.MongoClientSettings;
import com.mongodb.ConnectionString;
import com.mongodb.ServerAddress;
import com.mongodb.MongoCredential;
import com.mongodb.client.FindIterable;
import org.bson.Document;

import org.norelapi.core.UnsupportedOperationException;
import org.norelapi.core.DocumentManager;
import org.norelapi.core.SingleOperationResult;
import org.norelapi.core.BulkOperationResult;
import org.norelapi.core.OperationException;
import org.norelapi.impl.shared.GenericSingleOperationResult;
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
  private MongoDBLibrary library;

  protected MongoDBDocumentManager(MongoDBLibrary library) {
    this.library = library;
  }

  // Simple, required, operations

  public SingleOperationResult createDocument(String id,InputStream docSrc) throws OperationException {
    try {
      // create document and add to MongoDB
      Document document = Document.parse(readInputStreamIntoString(docSrc));
      if (null != id) {
        document.putIfAbsent("_id",id);
      }
      library.getCollection().insertOne(document);
      return new GenericSingleOperationResult(true,"Successfully created Document with _id: '" + id + "'","");
    } catch (Exception e) {
      return new GenericSingleOperationResult(false,"Error executing MongoDB listDocuments",e,"");
    }
  }

  protected static String readInputStreamIntoString(InputStream docSrc) throws OperationException {
    // read entire json as string in to memory (yuck!) - no built in Document from input stream in MongoDB!?!
    StringBuilder fileContents = new StringBuilder();
    Scanner scanner = new Scanner(((Readable)new BufferedReader(new InputStreamReader(docSrc)) )); // ensures entire stream read
    String lineSeparator = System.getProperty("line.separator");

    try {
      while(scanner.hasNextLine()) {
        fileContents.append(scanner.nextLine());
        if (scanner.hasNextLine()) {
          fileContents.append(lineSeparator);
        }
      }
    } finally {
      scanner.close();
    }
    return fileContents.toString();
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
      Document document = Document.parse(readInputStreamIntoString(docSrc));
      MongoCollection<Document> col = library.getCollection();
      col.replaceOne(Filters.eq("_id", new ObjectId(id)),document);
      return new GenericSingleOperationResult(true,"Successfully updated document with _id: '" + id + "'","");
    } catch (Exception e) {
      return new GenericSingleOperationResult(false,"Error executing MongoDB listDocuments",e,"");
    }
  }

  public SingleOperationResult updateDocument(String id,InputStream docSrc,Collection<String> collections) throws OperationException {
    collectionCheck(collections,"updateDocument(id,docSrc,collections)");
    return updateDocument(id,docSrc);
  }

  public SingleOperationResult deleteDocument(String id) throws OperationException {
    try {
      library.getCollection().deleteOne(Filters.eq("_id", new ObjectId(id)));
      return new GenericSingleOperationResult(true,"Successfully deleted document with _id: '" + id + "'","");
    } catch (Exception e) {
      return new GenericSingleOperationResult(false,"Error executing MongoDB listDocuments",e,"");
    }
  }

  public SingleOperationResult getDocument(String id) throws OperationException {
    try {
      Document document = library.getCollection().find(Filters.eq("_id", new ObjectId(id))).first();
      return new GenericSingleOperationResult(true,
        "Successfully retreived document with _id: '" + id + "'",document.toJson());
    } catch (Exception e) {
      return new GenericSingleOperationResult(false,"Error executing MongoDB listDocuments",e,"");
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
        new MongoDBFindIterator<SingleOperationResult>(iter));
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
      return new GenericBulkOperationResult(true,"Found documents",new MongoDBFindIterator<SingleOperationResult>(iter));
    } catch (Exception e) {
      return new GenericBulkOperationResult(false,"Error executing MongoDB findDocuments",e,null);
    }
  }

  // TODO complex, optional, operations
}