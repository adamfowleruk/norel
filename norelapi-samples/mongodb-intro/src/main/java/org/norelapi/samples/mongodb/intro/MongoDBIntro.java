package org.norelapi.samples.mongodb.intro;

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
  public static void main(String args[]) {
    try {
      ConnectionManager mgr = new ConnectionManager();

      // list drivers, to check they've loaded ok from classpath
      Collection<DriverInfo> drivers = mgr.listDriverInfos();
      for (DriverInfo driver: drivers) {
        System.out.println("Driver: " + driver.getAlias() + "(" + driver.getClassname() + ")");
      }

      Connection conn = mgr.getConnection("mongodb-local"); // see norelapi.connections.json in src/resources
      Library contacts = conn.getLibrary("contacts");
      DocumentManager docMgr = contacts.createDocumentManager();
      InputStream jsonStream = MongoDBIntro.class.getClassLoader().getResourceAsStream("contact.json");
      SingleOperationResult result = docMgr.createDocument("1",jsonStream);

      if (result.isSuccess()) {
        System.out.println("Yey! Success!!! : " + result.getMessage());
      } else {
        System.out.println("Computer says no! : " + result.getMessage());
        Exception ex = result.getException();
        if (null != ex) {
          ex.printStackTrace(System.out);
        }
      }
    } catch (OperationException oe) {
      oe.printStackTrace(System.out);
    }
  }
}