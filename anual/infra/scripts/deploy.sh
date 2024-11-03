SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
export HOST=$1

echo "\n========== Starting deployment =========="

ansible-playbook -u $USER "$SCRIPT_DIR/../ansible/server.yaml" -e "@deploy-conf.json" --limit $HOST
SCRIPT_DIR/certificate.sh

echo "\n========== Server deployment finished =========="
