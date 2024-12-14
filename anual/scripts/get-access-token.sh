ORIGIN=$1
MAIL=$2
PASSWORD=$3

curl -X POST -H 'Content-type: application/json' -d "{ \"mail\": \"$MAIL\", \"password\": \"$PASSWORD\" }"  $ORIGIN/user/token/
