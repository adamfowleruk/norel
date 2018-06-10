#!/bin/sh
unzip target/mongodb-intro-*.zip
java -cp target/mongodb-intro-1.0.jar:target/mongodb-intro-1.0/lib/norelapi-core-0.1.jar:target/mongodb-intro-1.0/lib/javax.json-api-1.1.2.jar:target/mongodb-intro-1.0/lib/javax.json-1.1.2.jar:target/mongodb-intro-1.0/lib/mongodb-driver-sync-3.7.1.jar:target/mongodb-intro-1.0/lib/mongodb-driver-core-3.7.1.jar:target/mongodb-intro-1.0/lib/bson-3.7.1.jar:src/main/resources org.norelapi.samples.mongodb.intro.MongoDBIntro

