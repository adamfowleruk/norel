# Unresolved design questions

## Document class

Do we support the concept that a document has a single class?

Do we support class hierarchies?

Single or multiple inheritance?

## Do we make SingleOperationResult and BulkOperationResult <typed> ?

Would aid in making conversion to/from JSON and other types more accessible to programmers.

Bulk must support multiple types within the full bulk response.

Would need to remain high level. E.g. SingleOperationResult<Document> or SingleOperationResult<KeyValue>

Would need a base Record class potentially (supporting getId() as a minimum, and any introspection functions)

## How to effectively track execution time without costly Java profilers

Custom PERF log4j log level?

## Leave as-is or implement missing paradigms in Core?

A record (document) having multiple logical collections is not supported by many document databases. E.g. MongoDB

Do we may a document on to a single collection (i.e. the physical library collection) in this instance, or
do we implement our own logical collection property on top?

Instinct is to not create our own extensions in Core - as this would necessitate introspection of all JSON
documents and modification of them, independent of developer action.

We could implement this in a higher level API. E.g.:-

```java 
RectifiedCollectionManager rcm = new RectifiedCollectionManager(myMongoDBDocumentManager);
JSONObject myDocument = ...;
String[] collections = {"col1","col2"};
SingleOperationResult<Document> result = rcm.insertDocument(myDocument,collections);
```

Chaining objects together like this is very similar to how java.io adds features in to streams. 

This seems more palatable to developers... Any thoughts people???

## Thread safety

Do we force this at the Connection level only (necessitating each thread to have its own connection),
or do we implement this within individual Manager functions?

## Write concern, ACID, etc.

Do we provide a mechanism for ensuring, as far as practicable, a level of write and read concern?

Do we therefore provide a high level abstraction of this, which must support all existing DBs levels of write concern?

Do we guarantee an equal or high level of concern to our list of supported concerns?

Do we provide an incredibly strict mode that saves a document, then queries it against a separate connection on a
separate host until it exists, to ensure ACID at a higher level than the database engine than the underlying connection
itself.

