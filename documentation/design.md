# Design document

This document describes the high level design of the NoREL NoSQL database abstraction library.

This document lists final design decisions. Undecided design ideas may be read about in the [Design Questions](design-questions.md) Document.

## Design principles

- Apache 2.0 licensed - allowing for maximum re-use by other libraries and applications, including commercial applications
- Use database-independent terminology across all underlying databases
  - Abstract away all connection configuration and handling
  - No point providing a 'database independent' library, if you then have to pass MongoDB specific connection settings in application code
- Designed with multi-model NoSQL databases in mind
  - Get a Connection first, then decide on your data model
  - Introspect your connection to determine what models and operations are supported
  - Blend models together - graphs relating to documents or key-value pairs
- Don't become the 'lowest common denominator'
  - Must allow access to DB specific features or extensions, if the developer wishes to cross in to that particular chasm
  - I.e. don't force them to use our API for 95% of the code, then have to use the vendors' underlying API anyway (and thus learn and entirely new paradigm) to access some new or fancy functionality we haven't implemented in Core.
- Lean and mean - high speed, low drag
  - Tuned for performance to place the minimum speed penalty on the library user
  - Applies best-practice from the underlying software vendors, to ensure maximum data throughput and response times
  - Support for underlying connection pooling, safe shutdown (or forced shutdown), and other high scale performance approaches
  - Won't automatically parse/serialise underlying JSON or XML strings without user action
  - May provide lazy iterators to prevent blocking. E.g. when fetching all documents within a collection across multiple hops
  - Must be thread safe
- Don't provide any higher level concepts/utilities in the core library
  - Data migration, writing to two documents/databases simultaneously, abstracted to higher level library
  - Not tied to any particular high level persistence framework - instead make it easy to incorporate those frameworks on top of this library
    - JPA, ORM, JDBC, Spring Data and other libraries should be layered over this library
- Don't force the use of a particular paradigm
  - Not everyone likes async processing, so eventually we'll provide both mechanisms, but for now we'll provide just synchronous (as most underlying APIs do the same)

## Slightly out of scope

There are many obvious tasks and extensions that may be layered over this library. Separate projects shall be created for those approaches.

## Database support

There are no good authoritative statistics available for the most popular NoSQL database. Although unreliable, the DB-engines ranking
is the best currently available. We'll use that to add supported databases to this library

### Impedence mismatch

It should be noted that each NoSQL database, just like their relational cousins, has a different level of storage management. 
Some database servers support more than one database within a single cluster. Others only support one. Some have the concept of
Collections or Tables that records live within. In others, a Document may exist within multiple or zero connections.

This leads to terminology hell, and confusion for the develop. I have had to settle on particular concepts. Not because I
religiously believe this is the 'right way' to do things, but out of necessity to aid the casual developer. My concepts are:-

- Top level ConnectionManager provided by the NoREL API. Get a connection first
 - ConnectionManager has classloader access to multiple NoSQL database Drivers
- A Driver maps on to the underlying connection technology used - a mix of database engine and communication protocol in use
 - A Driver may stand up multiple Connection instances, abstracted away from application code
- A Connection to a database server may be able to access one or more Libraries
 - A Connection has a state, and may be configured, connected to, and disconnected from
- A Library is a PHYSICAL separation of records (Documents etc)
 - A Library has indexes, storage settings, etc. - Just like a physical library building
 - A Library may be sharded across the database cluster
 - A record can only exist in one library
 - A Library MAY support Key management, Document management, Column management
  - Use createDocumentManager() or createKeyManager() to create a manager instance
  - Allows future advanced functionality to be added in future
  - Allows a single database connection to perform multiple model actions
 - A Library maps to:-
  - MongoDB Collection (confusingly...)
  - AWS DynamoDB Table
  - MarkLogic Server Database
- A Collection is a LOGICAL separation of records (Documents etc)
 - A Collection can have multiple Documents
 - In real life, a collection within a library may expand, contract, or be moved within the library. A Book (Document) may have multiple categorisations, and thus appear in separate collections within a Library
 - A Document may exist in zero or more collections (in some DB servers this may be limited to one or more, or exactly one, collections)
 - A Collection maps to:-
  - MongoDB Collection (confusingly. See Library, above) - albeit each document has exactly 1 collection in this instance
  - MarkLogic Server collection


## Feature compliance

Not all NoSQL databases will permit all functions. For example, some won't support free text search. There will have to be core
functionality followed by optional functionality, and a mechanism for introspecting what is supported by the underlying database.

### Core functions

- ConnectionFactory class - Connection handling for synchronous, stateful connections
  - Authentication mechanism support (user/pass, kerberos, etc)
  - A Connection can list many Libraries held in the database server
- Library concept for storage level abstrction (a Library has indexes, stores document/keys, is queriable in its entirety)
  - A Library may support one or more storage model managers - KeyValue, Document, Graph, Column
- A Manager manages records (E.g. Documents) within a library
  - A Document may exist in one or more logical collections

### Document functions

For a document orientated NoSQL databases, the minimum functionality would include:-

- Database level permissions
- CRUD operations - Create, Read, Update, Delete - for single documents
- Basic query operations for simple queries - E.g. fetch document(s) by a single indexed field

Advanced optional operations include:-

- Bulk document update/delete/read operations
 - With or without streaming support (i.e. just write 10 in one request, or write 100 over 10 requests)
- Free text search
- Complex logic with queries, including range index support and boolean logic operators
- Versioning support
- Separate properties (metadata) to the main document support
- Document level permissions

Other types of NoSQL database may be supported through different parent manager objects