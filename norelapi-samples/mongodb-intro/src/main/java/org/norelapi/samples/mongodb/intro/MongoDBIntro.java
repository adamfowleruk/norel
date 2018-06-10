package org.norelapi.samples.mongodb.intro;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.norelapi.core.ConnectionManager;
import org.norelapi.core.Connection;
import org.norelapi.core.Driver;
import org.norelapi.core.DriverInfo;
import org.norelapi.core.Library;
import org.norelapi.core.DocumentManager;
import org.norelapi.core.SingleOperationResult;
import org.norelapi.core.OperationException;

import java.io.InputStream;
import java.util.Collection;

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
      DocumentManager docMgr = contacts.createDocumentManager();
      InputStream jsonStream = MongoDBIntro.class.getClassLoader().getResourceAsStream("contact.json");
      SingleOperationResult result = docMgr.createDocument("1",jsonStream);

      if (result.isSuccess()) {
        logger.info("Yey! Success!!! : {}", result.getMessage());
      } else {
        logger.info("Computer says no! : {}", result.getMessage());
        Exception ex = result.getException();
        if (null != ex) {
          //ex.printStackTrace(System.out);
          logger.info(ex);
        }
      }
    } catch (OperationException oe) {
      //oe.printStackTrace(System.out);
      logger.info(oe);
    }
  }
}