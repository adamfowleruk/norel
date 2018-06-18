#!/bin/sh

cd ../../../dependencies/dynamodb
java -Djava.library.path=./DynamoDBLocal_lib -jar DynamoDBLocal.jar -sharedDb -inMemory
