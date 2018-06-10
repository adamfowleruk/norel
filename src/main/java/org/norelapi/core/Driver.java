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

import java.util.Hashtable;

/**
 * Very thin interface for a Driver. Required for implementation a NoSQL DB
 * Driver in the NoREL API.
 *
 * A Driver implementation should have ZERO class dependencies outside of the
 * Java runtime and the NoREL API. This is to allow the Driver implementation
 * to be loaded by the class loader in all circumstances, with no class not
 * found exceptions and no instantiation exceptions.
 *
 * This is required because we need the getInfo() function to always work.
 * Only the create connection method should throw ClassNotFoundException
 * when it tries to load or instantiate the Connection implementation class
 * by name ONLY.
 */
public interface Driver {
  public DriverInfo getInfo();

  // Internal methods
  public Connection createConnection(Hashtable<String,Object> config);

}