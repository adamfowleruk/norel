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

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import org.norelapi.core.Library;
import org.norelapi.core.LibraryInfo;
import org.norelapi.core.UnsupportedOperationException;
import org.norelapi.core.DocumentManager;
import org.norelapi.core.KeyManager;

/**
 * Implements the NoREL API Library interface for MongoDB.
 *
 * In MongoDB, unlike some Document databases, a Collection is the unit of 
 * storage, not a database. Thus a Library in the NoREL API is analogous to a
 * MongoDB Collection, NOT a Database.
 *
 * A Document in MongoDB can only be a member of one Collection. Index settings
 * are set against a Collection in MongoDB not a Database.
 */
public class MongoDBLibrary implements Library {
  private static final Logger logger = LogManager.getLogger();
  private MongoDBConnection connection;
  private MongoCollection<Document> collection;
  private String alias; // collection alias

  protected MongoDBLibrary(MongoDBConnection conn,String alias) {
    this.connection = conn;
    this.alias = alias;
    logger.trace("MongoDB Library(Collection) alias: {}",alias);
    MongoDatabase db = this.connection.getDatabase();
    logger.trace("MongoDB database: {}",db);
    collection = db.getCollection(alias);
  }
  
  public boolean hasKeyManager() throws UnsupportedOperationException {
    return false;
  }
  public boolean hasDocumentManager() throws UnsupportedOperationException {
    return true;
  }

  public boolean isOpen() {
    return true; // TODO verify this is always the case. Can it be unavailable whilst reindexing, starting up, etc.?
  }

  public LibraryInfo getInfo() {
    return new LibraryInfo(alias,true,false);
  }

  public DocumentManager createDocumentManager() throws UnsupportedOperationException {
    MongoDBDocumentManager mgr = new MongoDBDocumentManager(this);
    return mgr;
  }
  public KeyManager createKeyManager() throws UnsupportedOperationException {
    throw new org.norelapi.core.UnsupportedOperationException("MongoDB does not support Key-Value use","createKeyManager()");
  }

  // Internal methods - package visibility only
  protected MongoDBConnection getConnection() {
    return connection;
  }

  protected MongoCollection<Document> getCollection() {
    return collection;
  }

  protected String getAlias() {
    return alias;
  }
}