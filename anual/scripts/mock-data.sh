#!/bin/bash

# Number of samples to create
NUM_CONTRIBUTORS=50
NUM_MEAL_DONATIONS=100

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
mkdir -p "$SCRIPT_DIR/test-files"

CONTRIBUTOR_FILE="$SCRIPT_DIR/test-files/contributors.csv"
CONTRIBUTION_FILE="$SCRIPT_DIR/test-files/contribution.csv"
INDIVIDUAL_FILE="$SCRIPT_DIR/test-files/individuals.csv"
LEGAL_ENTITY_FILE="$SCRIPT_DIR/test-files/legal_entities.csv"
MEAL_DONATION_FILE="$SCRIPT_DIR/test-files/meal_donations.csv"

echo "id,address,points" > "$CONTRIBUTOR_FILE"
echo "id,date,contributor_id" > "$CONTRIBUTION_FILE"
echo "id,document,documentType,name,surname,birth" > "$INDIVIDUAL_FILE"
echo "id,type,category" > "$LEGAL_ENTITY_FILE"
echo "id,mealId" > "$MEAL_DONATION_FILE"

# Generate mock contributors and individual/legal entities
for ((i=1; i<=NUM_CONTRIBUTORS; i++))
do
    ADDRESS="Address_$i"
    POINTS=$(shuf -i 10-100 -n 1)
    echo "$i,$ADDRESS,$POINTS" >> "$CONTRIBUTOR_FILE"

    if (( RANDOM % 2 )); then
        DOCUMENT=$((100000000 + RANDOM % 900000000))
        NAME="Name_$i"
        SURNAME="Surname_$i"
        BIRTH="1990-01-01"
        DOCUMENT_TYPE="DNI"
        echo "$i,$DOCUMENT,$DOCUMENT_TYPE,$NAME,$SURNAME,$BIRTH" >> "$INDIVIDUAL_FILE"
    else
        TYPE="Company"
        CATEGORY=$(shuf -n 1 -e HealthCare Education Finance Technology Agriculture Hospitality Transportation Manufacturing Retail)
        echo "$i,$TYPE,$CATEGORY" >> "$LEGAL_ENTITY_FILE"
    fi
done

# Generate mock meal donations
for ((i=1; i<=NUM_MEAL_DONATIONS; i++))
do
    CONTRIBUTION_ID=$i
    CONTRIBUTOR_ID=$((1 + RANDOM % NUM_CONTRIBUTORS))
    DATE=`date +"%s"`
    MEAL_ID=NULL

    echo "$CONTRIBUTION_ID,$DATE,$CONTRIBUTOR_ID" >> $CONTRIBUTION_FILE 
    echo "$CONTRIBUTION_ID,$MEAL_ID" >> "$MEAL_DONATION_FILE"
done

echo "Mock data generated:"
echo "Contributors: $CONTRIBUTOR_FILE"
echo "Contributions: $CONTRIBUTION_FILE"
echo "Individuals: $INDIVIDUAL_FILE"
echo "Legal Entities: $LEGAL_ENTITY_FILE"
echo "Meal Donations: $MEAL_DONATION_FILE"
