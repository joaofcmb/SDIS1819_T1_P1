#!/bin/sh

CLASS_PATH="./build/production/SDIS1819_T1"
TESTAPP_MAIN="client.TestApp"

PEER_ID=$1
FILE_PATH=$2

java --class-path $CLASS_PATH $TESTAPP_MAIN "accesspoint$PEER_ID" "RESTORE" $FILE_PATH