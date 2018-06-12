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
package org.norelapi.samples.mongodb.intro;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.norelapi.core.ConnectionManager;
import org.norelapi.core.Connection;
import org.norelapi.core.Result;
import org.norelapi.core.Record;
import org.norelapi.core.Driver;
import org.norelapi.core.DriverInfo;
import org.norelapi.core.Library;
import org.norelapi.core.DocumentManager;
import org.norelapi.core.SingleOperationResult;
import org.norelapi.core.BulkOperationResult;
import org.norelapi.core.OperationException;
import org.norelapi.core.OperationResult;

import java.io.InputStream;
import java.util.Collection;
import java.util.Vector;
import java.util.Iterator;

public class MongoDBIntro {
  private static final Logger logger = LogManager.getLogger();
  public static void main(String args[]) {
    try {
      ConnectionManager mgr = new ConnectionManager();

      // list drivers, to check they've loaded ok from classpath
      Collection<DriverInfo> drivers = mgr.listDriverInfos();
      for (DriverInfo driver: drivers) {
        logger.info("Driver: {}({})",driver.getAlias(), driver.getClassname());
      }

      Connection conn = mgr.getConnection("mongodb-local"); // see norelapi.connections.json in src/resources
      conn.connect();
      Library contacts = conn.getLibrary("contacts");
      if (!contacts.hasDocumentManager()) { // could accidentally be pointing towards a K-V store!
        logger.info("Library does not support Document operations");
        System.exit(1);
      }
      DocumentManager docMgr = contacts.createDocumentManager();

      // 1. load a json document into MongoDB
      printSection("1. Load a JSON document in to MongoDB");
      InputStream jsonStream = MongoDBIntro.class.getClassLoader().getResourceAsStream("contact.json");
      SingleOperationResult result = docMgr.createDocument(null,jsonStream); // auto id

      printResult(result);

      // 2. go find these documents and list them
      printSection("2. List all documents in the MongoDB collection");
      BulkOperationResult batch = docMgr.listDocuments("contacts");
      Iterator<SingleOperationResult<Result>> iter = batch.getResultIterator();
      Vector<String> docIds = new Vector<String>();
      while (iter.hasNext()) {
        SingleOperationResult<Result> res = iter.next();
        printResult(res);
        Result content = res.getResult();
        System.out.println("JSON: " + content.getPayload());
        if (content instanceof Record) {
          docIds.add(((Record)content).getId());
        }
      }

      // 3. delete these documents
      printSection("3. Delete all these MongoDB documents");
      for (String id : docIds) {
        SingleOperationResult<Result> r = docMgr.deleteDocument(id);
        printResult(r,false);
      }

    } catch (OperationException oe) {
      //oe.printStackTrace(System.out);
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
    System.out.println("----------------------------------------");
    System.out.println(title);
  }
}