#!/bin/bash

ORIGIN=$1
CARD_NUMBER=$2
SECURITY_CODE=$3
FRIDGE_ID=$4
MEAL_IDS=$5

# We expect a comma separated list of meals_id 
MEALS_JSON=$(echo "$MEAL_IDS" | jq -R 'split(",") | map({id: .})')

curl -X POST -H 'Content-Type: application/json' -d \
    "{ 
      \"fridge_id\": \"$FRIDGE_ID\", 
      \"card\": { 
          \"number\": \"$CARD_NUMBER\", 
          \"security_code\": \"$SECURITY_CODE\" 
      }, 
      \"meals\": $MEALS_JSON
    }" \
    "$ORIGIN/pin/withdraw"
