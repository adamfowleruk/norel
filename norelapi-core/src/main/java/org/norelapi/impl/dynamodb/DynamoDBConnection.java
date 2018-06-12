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
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.amazonaws.services.dynamodbv2.document.TableCollection;

import org.norelapi.impl.shared.IOUtils;
import org.norelapi.core.UnsupportedOperationException;
import org.norelapi.core.DocumentManager;
import org.norelapi.core.SingleOperationResult;
import org.norelapi.core.ConnectionException;
import org.norelapi.core.Connection;
import org.norelapi.core.Connection.Status;
import org.norelapi.core.BulkOperationResult;
import org.norelapi.core.OperationException;
import org.norelapi.core.OperationException;
import org.norelapi.core.NoSuchEntityException;
import org.norelapi.core.InvalidStateException;
import org.norelapi.core.Library;
import org.norelapi.core.LibraryInfo;
import org.norelapi.impl.shared.GenericBulkOperationResult;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Hashtable;
import java.util.Collection;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class DynamoDBConnection implements Connection {
  private static final Logger logger = LogManager.getLogger();
  private Hashtable<String,Object> config = null;
  private Status status = Status.Initialising;

  private AmazonDynamoDB client = null;
  private DynamoDB dynamodb = null;

  /** Default constructor for class loading */
  public DynamoDBConnection(){}

  public Status getStatus() {
    return status;
  }

  public void configure(Hashtable<String,Object> config) throws OperationException {
    this.config = config;
    status = Status.Initialised;
  }

  private void connectedCheck() throws InvalidStateException {
    if (Status.Connected != status) {
      throw new InvalidStateException("Cannot proceed - not connected",Status.Connected.toString(),status.toString());
    }
  }

  public void connect() throws ConnectionException {
    status = Status.Connecting;

    String dbName = (String)config.get(DynamoDBDriver.DATABASE);
    String username = (String)config.get(DynamoDBDriver.USERNAME);
    String password = ((String)config.get(DynamoDBDriver.PASSWORD));
    if (null != username) {
      logger.trace("Username and password set");
      // TODO set credential class here
    }
    String hostStr = (String)config.get(DynamoDBDriver.CONNECTION_STRING); // E.g. "http://localhost:8000"
    String region = (String)config.get(DynamoDBDriver.REGION); // E.g. "us-west-2"

    // TODO something with the config here
    client = AmazonDynamoDBClientBuilder.standard()
      .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(hostStr, region))
      .build();

    dynamodb = new DynamoDB(client);
    
    status = Status.Connected;
  }
  public void disconnect() throws ConnectionException {
    status = Status.Disconnecting;

    // TODO actually close connection, and free up underlying resources

    status = Status.Disconnected;
  }
  public void forceDisconnect() throws ConnectionException {
    status = Status.Disconnecting;

    // TODO actually close connection, and free up underlying resources

    status = Status.Disconnected;
  }

  public Collection<LibraryInfo> listLibraries() throws InvalidStateException {
    connectedCheck();
    ArrayList<LibraryInfo> info = new ArrayList<LibraryInfo>();

    TableCollection<ListTablesResult> tables = dynamodb.listTables();
    Iterator<Table> iterator = tables.iterator();

    logger.trace("Listing table names");

    while (iterator.hasNext()) {
      Table table = iterator.next();
      logger.trace("Table: {}",table.getTableName());
      info.add(new LibraryInfo(table.getTableName(),true,true));
    }

    return info;
  }
  public Library getLibrary(String alias) throws NoSuchEntityException, InvalidStateException {
    connectedCheck();
    DynamoDBLibrary library = new DynamoDBLibrary(this,alias);
    // TODO we should probably track these internally...
    return library;
  }

  // protected internal methods only
  protected AmazonDynamoDB getClient() {
    return client;
  }
  protected DynamoDB getDynamoDB() {
    return dynamodb;
  }
}