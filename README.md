# NoREL API

Abstraction API for NoSQL non-relational document databases.

## Core API

The core API provides a complete abstraction of underlying NoSQL database configuration,
connection handling, and basic operations on a catalog (aka database) of documents.

Currently Document databases are supported. Other types of NoSQL database may be added in future.

## Installation

TODO Maven instructions

## Usage

The following shows how to use the API with MongoDB:-

```java
import org.norelapi.core.*;
...
// Set up the connection and catalog we're accessing
ConnectionManager manager = new ConnectionManager();
Connection mongoConn = manager.getConnection("mymongodbconfig");
Library salesCollection = mongoConn.getLibrary("sales");
// Now perform document operations
DocumentManager docManager = salesCollection.getDocumentManager();
SingleOperationResult result = docManager.addDocument("sale1234.json",someJsonString);
if (result.isSuccess()) {
  System.out.println("Yay! It worked!");
} else {
  System.out.println("Computer says no! : " + result.getMessage());
}
```

## Design

The high level design can be read about in the [Design document](documentation/design.md).

## License

This code is licensed under the Apache 2.0 license, and is copyright 2018 Adam Fowler.

## Using the API

Download a copy of the API from the [Releases page](https://github.com/adamfowleruk/norel/releases).
