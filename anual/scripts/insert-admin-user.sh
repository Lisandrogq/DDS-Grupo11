#!/bin/bash

DB_NAME=`jq -r ".postgres_db" $1`
DB_USER=`jq -r ".postgres_user" $1`
DB_PASSWORD=`jq -r ".postgres_password" $1`
OWNER_ID=$2
MAIL=$3
PASSWORD_HASH=$4

ID=$((RANDOM + OWNER_ID))

ssh fridge-bridge-aws << EOF
  export PGPASSWORD="$DB_PASSWORD"
  sql="INSERT INTO credentials (id, ownerId, mail, password, userType, provider) VALUES ($ID,$OWNER_ID, '$MAIL', '$PASSWORD_HASH', 'Admin', 'FridgeBridge');"
  psql -U "$DB_USER" -d "$DB_NAME" -c "\$sql"
  unset PGPASSWORD
EOF
