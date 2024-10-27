#!/bin/bash

# Database credentials
DB_USER="postgres"
DB_PASS="postgres"
DB_NAME="postgres"

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
CONTRIBUTOR_FILE="$SCRIPT_DIR/test-files/contributors.csv"
CONTRIBUTION_FILE="$SCRIPT_DIR/test-files/contribution.csv"
INDIVIDUAL_FILE="$SCRIPT_DIR/test-files/individuals.csv"
LEGAL_ENTITY_FILE="$SCRIPT_DIR/test-files/legal_entities.csv"
MEAL_FILE="$SCRIPT_DIR/test-files/meal.csv"
MEAL_DONATION_FILE="$SCRIPT_DIR/test-files/meal_donations.csv"

function execute_sql {
    local sql="$1"
    docker exec fridge-bridge-postgres-1 psql -U "$DB_USER" -d "$DB_NAME" -c "$sql" > /dev/null
}

echo "Inserting contributors..."
while IFS=, read -r id address points; do
    if [[ $id == "id" ]]; then
        continue
    fi
    sql="INSERT INTO contributor (id, address, points) VALUES ($id, '$address', $points);"
    execute_sql "$sql"
done < "$CONTRIBUTOR_FILE"

echo "Inserting individuals..."
while IFS=, read -r id document documentType name surname birth; do
    if [[ $id == "id" ]]; then
        continue
    fi
    sql="INSERT INTO individual (id, document, documentType, name, surname, birth) VALUES ($id, $document, '$documentType', '$name', '$surname', '$birth');"
    execute_sql "$sql"
done < "$INDIVIDUAL_FILE"

echo "Inserting legal entities..."
while IFS=, read -r id type category; do
    if [[ $id == "id" ]]; then
        continue
    fi
    sql="INSERT INTO legalentity (id, type, category) VALUES ($id, '$type', '$category');"
    execute_sql "$sql"
done < "$LEGAL_ENTITY_FILE"

echo "Inserting contributions..."
while IFS=, read -r id date contributor_id; do
    if [[ $id == "id" ]]; then
        continue
    fi
    sql="INSERT INTO contribution (id, date, contributor_id) VALUES ($id, '$date', '$contributor_id');"
    execute_sql "$sql"
done < "$CONTRIBUTION_FILE"

echo "Inserting meal donations..."
while IFS=, read -r id mealId; do
    if [[ $id == "id" ]]; then
        continue
    fi
    sql="INSERT INTO mealdonation (id, meal_id) VALUES ($id, $mealId);"
    execute_sql "$sql"
done < "$MEAL_DONATION_FILE"

echo "Data has been successfully inserted into the database."
echo "If you are on a test enviroment, you can see it through adminer:  http://localhost:8080/?pgsql=postgres&username=postgres&db=postgres&ns=public"
