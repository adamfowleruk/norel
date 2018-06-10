#!/bin/sh
cd target 
unzip -u -o mongodb-intro-*.zip
cd ..
LIBS=target/mongodb-intro-1.0/lib
java -cp "target/mongodb-intro-1.0.jar:$LIBS/norelapi-core-0.1.jar:$LIBS/javax.json-api-1.1.2.jar:$LIBS/javax.json-1.1.2.jar:$LIBS/mongodb-driver-sync-3.7.1.jar:$LIBS/mongodb-driver-core-3.7.1.jar:$LIBS/bson-3.7.1.jar:$LIBS/log4j-api-2.11.0.jar:$LIBS/log4j-core-2.11.0.jar:src/main/resources:." org.norelapi.samples.mongodb.intro.MongoDBIntro

