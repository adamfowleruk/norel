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

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.MongoCredential;

import org.norelapi.core.Connection;
import org.norelapi.core.ConnectionException;
import org.norelapi.core.InvalidStateException;
import org.norelapi.core.NoSuchEntityException;
import org.norelapi.core.OperationException;
import org.norelapi.core.Library;
import org.norelapi.core.LibraryInfo;

import java.util.Collection;
import java.util.Hashtable;
import java.util.ArrayList;

/**
 * MongoDB implementation of the NoREL API Connection interface.
 */
public class MongoDBConnection implements Connection {
  private static final Logger logger = LogManager.getLogger();
  private Hashtable<String,Object> config = null;
  private Status status = Status.Initialising;

  private MongoClient mongoClient = null;
  private MongoClientSettings options = null;
  private MongoCredential credential = null;
  private MongoDatabase database = null;

  /** 
   * Default constructor for dynamic class loading
   */
  public MongoDBConnection() {}

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
    // TODO ensure this is shared between all MongoDB Connection instances with the same config
    // As per: http://mongodb.github.io/mongo-java-driver/3.7/driver/tutorials/connect-to-mongodb/
    MongoClientSettings.Builder builder = MongoClientSettings.builder();
    boolean withSsl = ((Boolean)config.get(MongoDBDriver.SSL_ENABLED)).booleanValue();

    String dbName = (String)config.get(MongoDBDriver.DATABASE);
    String username = (String)config.get(MongoDBDriver.USERNAME);
    String password = ((String)config.get(MongoDBDriver.PASSWORD));
    if (null != username) {
      logger.trace("Username and password set");
      credential = MongoCredential.createCredential(
        username , 
        dbName, 
        password.toCharArray()
      );
    }
    ArrayList<ServerAddress> servers = new ArrayList<ServerAddress>();
    String hostStr = (String)config.get(MongoDBDriver.CONNECTION_STRING);
    String[] hosts = hostStr.split(",");
    for (String host : hosts) {
      logger.trace("Host: {}",host);
      String[] parts = host.split(":");
      logger.trace("Host part: {}",parts[0]);
      int port = 27017;
      if (parts.length > 1) {
        logger.trace("Port part: "+parts[1]);
        port = Integer.parseInt(parts[1]);
      }
      servers.add(new ServerAddress(parts[0],port));
    }
    if (null != credential) {
      logger.trace("Credentials set");
      builder = builder.credential(credential);
    }
    builder = builder.applyToSslSettings(bd -> bd.enabled(withSsl))
      .applyToClusterSettings(bd ->bd.hosts(servers)); 
    options = builder.build();
    logger.trace("Creating MongoDBClient");
    mongoClient = MongoClients.create(
      options
    );
    logger.trace("Fetching DB named: {}",dbName);
    database = mongoClient.getDatabase(dbName);
    logger.trace("MongoDB Database: {}",database);
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
    MongoIterable<String> collectionNames = database.listCollectionNames();
    for (String name: collectionNames) {
      info.add(new LibraryInfo(name,true,false));
    }
    return info;
  }

  public SingleOperationResult createLibrary(String alias) throws OperationException {
    try {
      
      return new SingleOperationResult<Result>(true,"Successfully created library (MongoDB Collection)",new Result(alias));
    } catch (Exception e) {
      return new SingleOperationResult<Result>(false,"Successfully created library (MongoDB Collection)",e);
    }
  }

  public Library getLibrary(String alias) throws NoSuchEntityException, InvalidStateException {
    connectedCheck();
    MongoDBLibrary library = new MongoDBLibrary(this,alias);
    // TODO we should probably track these internally...
    return library;
  }

  // protected internal methods only
  protected MongoClient getClient() {
    return mongoClient;
  }
  protected MongoDatabase getDatabase() {
    return database;
  }
}