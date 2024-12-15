ORIGIN=$1
FRIDGE_ID=$2
SENSOR_ID=$3
TEMPERATURE=$4
ACCESS_TOKEN=$5

curl -X POST -H 'Content-type: application/json' -H "Authorization: Bearer $ACCESS_TOKEN" -d "{ \"fridge_id\": \"$FRIDGE_ID\", \"sensor_id\": \"$SENSOR_ID\", \"temp\": $TEMPERATURE }"  $ORIGIN/fridge/sensor/temperature
