SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
CONFIG_FILE=$SCRIPT_DIR/deploy-conf.json

ANSIBLE_HOST=`jq -r ".host_ansible" $CONFIG_FILE`
HOST=`jq -r ".host" $CONFIG_FILE`
USER=`jq -r ".user" $CONFIG_FILE`

echo "\n========== Starting deployment =========="

ansible-playbook -u $USER "$SCRIPT_DIR/../ansible/server.yaml" -e "@$CONFIG_FILE" --limit $ANSIBLE_HOST

if [ $? -eq 0 ]; then
    "$SCRIPT_DIR/certificate.sh" "$USER" "$HOST"
else
    echo "Ansible playbook failed. Skipping certificate script."
fi

echo "\n========== Server deployment finished =========="
