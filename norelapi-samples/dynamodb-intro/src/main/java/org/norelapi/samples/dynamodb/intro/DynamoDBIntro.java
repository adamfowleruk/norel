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
package org.norelapi.samples.dynamodb.intro;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.norelapi.core.ConnectionManager;
import org.norelapi.core.Connection;
import org.norelapi.core.Result;
import org.norelapi.core.Record;
import org.norelapi.core.KeyValue;
import org.norelapi.core.Driver;
import org.norelapi.core.DriverInfo;
import org.norelapi.core.Library;
import org.norelapi.core.KeyManager;
import org.norelapi.core.SingleOperationResult;
import org.norelapi.core.BulkOperationResult;
import org.norelapi.core.OperationException;
import org.norelapi.core.OperationResult;

import java.io.InputStream;
import java.util.Collection;
import java.util.Vector;
import java.util.Iterator;

public class DynamoDBIntro {
  private static final Logger logger = LogManager.getLogger();
  public static void main(String args[]) {
    try {
      ConnectionManager mgr = new ConnectionManager();

      // list drivers, to check they've loaded ok from classpath
      Collection<DriverInfo> drivers = mgr.listDriverInfos();
      for (DriverInfo driver: drivers) {
        logger.info("Driver: {}({})",driver.getAlias(), driver.getClassname());
      }

      Connection conn = mgr.getConnection("dynamodb-local"); // see norelapi.connections.json in src/resources
      conn.connect();

      String tableName = "sales";
      String id = "123";

      Collection<LibraryInfo> libraries = mgr.listLibraries();
      boolean gotLib = false;
      for (LibraryInfo info: libraries) {
        if (info.getAlias().equals(tableName)) {
          gotLib = true;
        }
      }
      if (!gotLib) {
        // create table in dynamodb
        SingleOperationResult<Result> cr = mgr.createLibrary(tableName);
        printResult(cr,true);
      }


      Library sales = conn.getLibrary(tableName);
      if (!sales.hasKeyManager()) { // could accidentally be pointing towards a K-V store!
        logger.info("Library does not support KeyValue operations");
        System.exit(1);
      }
      KeyManager keyMgr = sales.createKeyManager();

      // 1. load a value in to DynamoDB
      printSection("1. Load a String value in to DynamoDB");
      SingleOperationResult result = keyMgr.putKey(id,"some value");
      printResult(result);

      // 2. go find these keys and list them
      printSection("2. Fetch key");
      SingleOperationResult<KeyValue> item = keyMgr.getKey(id);
      printResult(item,true);

      // 3. delete the key
      printSection("3. Delete the DynamoDB Key");
      SingleOperationResult<Result> r = keyMgr.deleteKey(id);
      printResult(r,false);

    } catch (OperationException oe) {
      logger.info(oe);
    }
  }

  private static void printResult(OperationResult result) {
    printResult(result,true);
  }

  private static void printResult(OperationResult result, boolean quitOnFail) {
    if (result.isSuccess()) {
      logger.info("Yey! Success!!! : {}", result.getMessage());
    } else {
      logger.info("Computer says no! : {}", result.getMessage());
      Exception ex = result.getException();
      if (null != ex) {
        //ex.printStackTrace(System.out);
        logger.info(ex);
      }
      if (quitOnFail) {
        System.exit(1); // quit here
      }
    }
  }

  private static void printSection(String title) {
    logger.info("----------------------------------------");
    logger.info(title);
  }
}