# Design document

This document describes the high level design of the NoREL NoSQL database abstraction library

## Design principles

- Not tied to any particular high level persistence framework - instead make it easy to incorporate those frameworks on top of this library
  - JPA, ORM, JDBC, Spring Data and other libraries should be layered over this library
  - No additional dependencies on these high level technologies are imported by this library
- Use database-independent terminology across all underlying databases
- Don't provide any higher level utilities in the core library
  - Data migration, writing to two documents/databases simultaneously, abstracted to higher level library
- Lean and mean - high speed, low drag
  - Tuned for performance to place the minimum speed penalty on the library user
  - Applies best-practice from the underlying software vendors, to ensure maximum data throughput and response times
  - Support for underlying connection pooling, safe shutdown (or forced shutdown), and other high scale performance approaches
- Apache 2.0 licensed - allowing for maximum re-use by other libraries and applications, including commercial applications

## Slightly out of scope

There are many obvious tasks and extensions that may be layered over this library. Separate projects shall be created for those approaches.

## Database support

There are no good authoritative statistics available for the most popular NoSQL database. Although unreliable, the DB-engines ranking
is the best currently available. We'll use that to add supported databases to this library

## Feature compliance

Not all NoSQL databases will permit all functions. For example, some won't support free text search. There will have to be core
functionality followed by optional functionality, and a mechanism for introspecting what is supported by the underlying database.

### Core functions

For a document orientated NoSQL databases, the minimum functionality would include:-

- Connection handling for synchronous, stateful connections
- Authentication mechanism support (user/pass, kerberos, etc)
- Database level permissions
- CRUD operations - Create, Read, Update, Delete - for single documents
- Basic query operations for simple queries - E.g. fetch document(s) by a single indexed field

Advanced optional operations include:-

- Bulk document update/delete/read operations
- Free text search
- Versioning support
- Separate properties (metadata) to the main document support
- Document level permissions

Other types of NoSQL database may be supported through different parent manager objects