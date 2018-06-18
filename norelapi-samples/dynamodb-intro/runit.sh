#!/bin/sh
cd target 
unzip -u -o dynamodb-intro-*.zip
cd ..
LIBS=target/dynamodb-intro-1.0/lib

java -cp "target/dynamodb-intro-1.0.jar:$LIBS/norelapi-core-0.1.jar:$LIBS/javax.json-api-1.1.2.jar:$LIBS/javax.json-1.1.2.jar:$LIBS/dynamodb-driver-sync-3.7.1.jar:$LIBS/dynamodb-driver-core-3.7.1.jar:$LIBS/bson-3.7.1.jar:$LIBS/log4j-api-2.11.0.jar:$LIBS/log4j-core-2.11.0.jar:src/main/resources:$LIBS/aws-java-sdk-core-1.11.344.jar:$LIBS/aws-java-sdk-dynamodb-1.11.344.jar:$LIBS/aws-java-sdk-kms-1.11.344.jar:$LIBS/aws-java-sdk-s3-1.11.344.jar:$LIBS/commons-logging-1.1.3.jar:$LIBS/jackson-core-2.6.7.jar:$LIBS/jackson-annotations-2.6.0.jar:$LIBS/jackson-dataformat-cbor-2.6.7.jar:$LIBS/jackson-databind-2.6.7.1.jar:$LIBS/httpclient-4.5.5.jar:$LIBS/httpcore-4.4.9.jar:$LIBS/joda-time-2.8.1.jar:." org.norelapi.samples.dynamodb.intro.DynamoDBIntro

