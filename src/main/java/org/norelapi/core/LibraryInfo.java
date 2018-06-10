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
 * Details about an available Library. 
 *
 * A Library is a physical unit of storage.
 * Think real library building. A library has a place for it's index, but it's
 * contained collection may move around of vary in size within the Library's
 * building boundaries.
 *
 * Some NoSQL databases call this a 'Database', but some, confusingly, call it
 * a 'Collection'. In this scenario, these databases (MongoDB, Cosmos DB) only
 * allow a document to be a member of a single collection. Also indexes are
 * set against a collection. Thus, logically, it is a NoREL API Library,
 * not a NoREL API Collection.
 */
public class LibraryInfo {
  private String alias = "";
  private boolean supportsDocumentManager = true;

  public LibraryInfo(String alias, boolean supportsDocMgr) {
    this.alias = alias;
    this.supportsDocumentManager = supportsDocMgr;
  }

  public String getAlias() {
    return alias;
  }

  public boolean hasDocumentManager() {
    return supportsDocumentManager;
  }
}