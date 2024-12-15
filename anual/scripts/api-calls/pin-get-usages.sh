#!/bin/bash

ORIGIN=$1
CARD_NUMBER=$2
SECURITY_CODE=$3

curl -X POST -H 'Content-type: application/json' -d "{ \"card\": { \"number\": \"$CARD_NUMBER\", \"security_code\": \"$SECURITY_CODE\" } }" "$ORIGIN/pin/usages"
