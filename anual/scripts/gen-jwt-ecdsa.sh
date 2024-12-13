openssl ecparam -name prime256v1 -genkey -noout -out jwt.ecdsa-private.pem > /dev/null 2>&1
openssl pkcs8 -topk8 -nocrypt -in jwt.ecdsa-private.pem -out jwt.ecdsa-private-pkcs8.pem > /dev/null 2>&1
openssl ec -in jwt.ecdsa-private.pem -pubout -out jwt.ecdsa-public.pem > /dev/null 2>&1

echo "=============== ECDSA Private Key ==============="
sed '1d;$d' jwt.ecdsa-private-pkcs8.pem

echo ""

echo "=============== ECDSA Public Key ==============="
sed '1d;$d' jwt.ecdsa-public.pem

rm jwt.ecdsa-private.pem jwt.ecdsa-private-pkcs8.pem jwt.ecdsa-public.pem
