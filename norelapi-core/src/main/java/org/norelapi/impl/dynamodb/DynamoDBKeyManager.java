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

import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.DeleteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.GetItemOutcome;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;

import org.norelapi.impl.shared.IOUtils;
import org.norelapi.impl.shared.SimpleKeyValue;
import org.norelapi.core.UnsupportedOperationException;
import org.norelapi.core.KeyManager;
import org.norelapi.core.Result;
import org.norelapi.core.KeyValue;
import org.norelapi.core.SingleOperationResult;
import org.norelapi.core.BulkOperationResult;
import org.norelapi.core.OperationException;
import org.norelapi.impl.shared.GenericBulkOperationResult;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Collection;
import java.util.Scanner;

import javax.naming.OperationNotSupportedException;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class DynamoDBKeyManager implements KeyManager {
  private static final Logger logger = LogManager.getLogger();
  private DynamoDBLibrary library;

  public DynamoDBKeyManager(DynamoDBLibrary library) {
    this.library = library;
  }

  public SingleOperationResult putKey(String key,InputStream value) throws OperationException {
    throw new UnsupportedOperationException("Operation is not supported by DynamoDB","putKey(String key,InputStream value)");
  }

  public SingleOperationResult putKey(String key,String value) throws OperationException {
    Table table = library.getTable();
    try {
      Item item = new Item().withPrimaryKey("Id", key).withString("Value", value);
      PutItemOutcome outcome = table.putItem(item);
      return new SingleOperationResult<Result>(true,"Successfully created Key with Id: '" + key + "'",new Result());
    } catch (Exception e) {
      return new SingleOperationResult<Result>(false,"Error executing DynamoDB putKey(String key,String value)",e);
    }
  }
  public SingleOperationResult putKey(String key,Serializable value) throws OperationException {
    throw new UnsupportedOperationException("Operation is not supported by DynamoDB","putKey(String key,Serializable value)");
  }

  public SingleOperationResult getKey(String key) throws OperationException {
    Table table = library.getTable();
    try {
      Item item = table.getItem("Id", key);
      return new SingleOperationResult<KeyValue>(true,"Successfully fetched Key with Id: '" + key + "'",
        new SimpleKeyValue<String,String>(key, item.getString("Value")));
    } catch (Exception e) {
      return new SingleOperationResult<KeyValue>(false,"Error executing DynamoDB getKey(String key)",e);
    }
  }
  
  public SingleOperationResult deleteKey(String key) throws OperationException {
    Table table = library.getTable();
    try {
      DeleteItemOutcome outcome = table.deleteItem("Id", key);
      return new SingleOperationResult<Result>(true,"Successfully deletec Key with Id: '" + key + "'",new Result());
    } catch (Exception e) {
      return new SingleOperationResult<Result>(false,"Error executing DynamoDB deleteKey(String key)",e);
    }
  }
}