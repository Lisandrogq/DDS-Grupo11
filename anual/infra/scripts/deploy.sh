SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
CONFIG_FILE=$SCRIPT_DIR/deploy-conf.json

ANSIBLE_HOST=`jq -r ".host_ansible" $CONFIG_FILE`
USER=`jq -r ".user" $CONFIG_FILE`

echo "\n========== Starting deployment =========="
ansible-playbook -u $USER "$SCRIPT_DIR/../ansible/server.yaml" -e "@$CONFIG_FILE" --limit $ANSIBLE_HOST
echo "\n========== Server deployment finished =========="
