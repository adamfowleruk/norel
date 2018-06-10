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

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.MongoCredential;

import org.norelapi.core.Connection;
import org.norelapi.core.ConnectionException;
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

  public void connect() throws ConnectionException {
    status = Status.Connecting;
    // TODO ensure this is shared between all MongoDB Connection instances with the same config
    // As per: http://mongodb.github.io/mongo-java-driver/3.7/driver/tutorials/connect-to-mongodb/
    MongoClientSettings.Builder builder = MongoClientSettings.builder();
    boolean withSsl = ((Boolean)config.get(MongoDBDriver.SSL_ENABLED)).booleanValue();

    String dbName = (String)config.get(MongoDBDriver.DATABASE);
    credential = MongoCredential.createCredential(
      (String)config.get(MongoDBDriver.USERNAME), 
      dbName, 
      ((String)config.get(MongoDBDriver.PASSWORD)).toCharArray()
    );
    ArrayList<ServerAddress> servers = new ArrayList<ServerAddress>();
    String hostStr = (String)config.get(MongoDBDriver.CONNECTION_STRING);
    String[] hosts = hostStr.split(",");
    for (String host : hosts) {
      String[] parts = host.split(":");
      int port = 27017;
      if (parts.length > 0) {
        port = Integer.parseInt(parts[1]);
      }
      servers.add(new ServerAddress(parts[0],port));
    }
    builder = builder.credential(credential).applyToSslSettings(bd -> bd.enabled(withSsl))
      .applyToClusterSettings(bd ->bd.hosts(servers)); 
    options = builder.build();
    mongoClient = MongoClients.create(
      options
    );
    database = mongoClient.getDatabase(dbName);
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

  public Collection<LibraryInfo> listLibraries() {
    ArrayList<LibraryInfo> info = new ArrayList<LibraryInfo>();
    MongoIterable<String> collectionNames = database.listCollectionNames();
    for (String name: collectionNames) {
      info.add(new LibraryInfo(name,true));
    }
    return info;
  }
  public Library getLibrary(String alias) throws NoSuchEntityException {
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