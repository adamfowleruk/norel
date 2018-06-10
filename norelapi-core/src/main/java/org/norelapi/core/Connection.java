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

import java.util.Collection;
import java.util.Hashtable;

/**
 * High level Connection interface to an underlying NoSQL database.
 * 
 * Holds the minimum information that a client application would need. 
 * Exact connection parameters are managed by a ConnectionFactory class.
 */
public interface Connection {
  public enum Status {
    Initialising,Initialised,Connecting,Connected,Disconnecting,Disconnected,Destroying
  }
  public Status getStatus();

  public void configure(Hashtable<String,Object> configuration) throws OperationException;
  public void connect() throws ConnectionException;
  public void disconnect() throws ConnectionException;
  public void forceDisconnect() throws ConnectionException;

  public Collection<LibraryInfo> listLibraries() throws InvalidStateException;
  public Library getLibrary(String alias) throws NoSuchEntityException,InvalidStateException;
}