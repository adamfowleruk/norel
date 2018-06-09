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

import org.norelapi.core.Catalog;
import org.norelapi.core.CatalogInfo;
import org.norelapi.core.DocumentManager;

/**
 * Implements the NoREL API Catalog interface for MongoDB.
 *
 * In MongoDB, unlike some Document databases, a Collection is the unit of 
 * storage, not a database. Thus a Catalog in the NoREL API is analogous to a
 * MongoDB Collection, NOT a Database.
 *
 * A Document in MongoDB can only be a member of one Collection. Index settings
 * are set against a Collection in MongoDB not a Database.
 */
public class MongoDBCatalog implements Catalog {

  public boolean isOpen() {

  }

  public CatalogInfo getInfo() {

  }

  public DocumentManager createDocumentManager() {
    MongoDBDocumentManager mgr = new MongoDBDocumentManager(this);
    return mgr;
  }
}