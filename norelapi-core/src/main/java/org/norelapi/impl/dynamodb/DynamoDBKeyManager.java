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

import org.norelapi.impl.shared.IOUtils;
import org.norelapi.core.UnsupportedOperationException;
import org.norelapi.core.KeyManager;
import org.norelapi.core.SingleOperationResult;
import org.norelapi.core.BulkOperationResult;
import org.norelapi.core.OperationException;
import org.norelapi.impl.shared.GenericBulkOperationResult;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Collection;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class DynamoDBKeyManager implements KeyManager {
  private static final Logger logger = LogManager.getLogger();
  private DynamoDBLibrary library;

  public DynamoDBKeyManager(DynamoDBLibrary library) {
    this.library = library;
  }


  public SingleOperationResult putKey(String key,InputStream value) throws OperationException {
    return null;
  }
  public SingleOperationResult putKey(String key,String value) throws OperationException {
    return null;
  }
  public SingleOperationResult putKey(String key,Serializable value) throws OperationException {
    return null;
  }

  public SingleOperationResult getKey(String key) throws OperationException {
    return null;
  }
  
  public SingleOperationResult deleteKey(String key) throws OperationException {
    return null;
  }
}