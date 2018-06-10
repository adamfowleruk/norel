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

import org.norelapi.core.Connection;
import org.norelapi.core.Connection.Status;
import org.norelapi.core.ConnectionException;
import org.norelapi.core.NoSuchEntityException;
import org.norelapi.core.Catalog;
import org.norelapi.core.CatalogInfo;

import java.util.Collection;

/**
 * MongoDB implementation of the NoREL API Connection interface.
 */
public class MongoDBConnection implements Connection {
  private Hashtable<String,Object> config = null;
  private Status status = Status.Initialising;

  public MongoDBConnection() {
    // default constructor for dynamic class loading
  }

  public Status getStatus() {

  }

  protected boolean configure(Hashtable<String,Object> config) {
    this.config = config;
  }

  public void connect() throws ConnectionException {

  }
  public void disconnect() throws ConnectionException {

  }
  public void forceDisconnect() throws ConnectionException {

  }

  public Collection<CatalogInfo> listCatalogs() {

  }
  public Catalog getCatalog(String alias) throws NoSuchEntityException {

  }
}