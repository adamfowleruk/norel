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

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;

import org.norelapi.core.Library;
import org.norelapi.core.LibraryInfo;
import org.norelapi.core.UnsupportedOperationException;
import org.norelapi.core.DocumentManager;
import org.norelapi.core.KeyManager;

public class DynamoDBLibrary implements Library {
  private static final Logger logger = LogManager.getLogger();
  private DynamoDBConnection connection;
  private Table table;
  private String alias; // collection alias

  public DynamoDBLibrary(DynamoDBConnection conn,String alias) {
    this.connection = conn;
    this.alias = alias;
    logger.trace("DynamoDB Library(Table) alias: {}",alias);
    DynamoDB db = this.connection.getDynamoDB();
    logger.trace("DynamoDB database: {}",db);
    table = dynamoDB.getTable(alias);
  }

  public boolean isOpen() {
    return true; // TODO verify this is always the case. Can it be unavailable whilst reindexing, starting up, etc.?
  }

  public LibraryInfo getInfo() {
    return new LibraryInfo(alias,true,true);
  }

  public DocumentManager createDocumentManager() {
    DynamoDBDocumentManager mgr = new DynamoDBDocumentManager(this);
    return mgr;
  }

  public KeyManager createKeyManager() {
    DynamoDBKeyManager mgr = new DynamoDBKeyManager(this);
    return mgr;
  }

  // Internal methods - package visibility only
  protected DynamoDBConnection getConnection() {
    return connection;
  }

  protected Table getTable() {
    return table;
  }

  protected String getAlias() {
    return alias;
  }

}