USER=$1
HOST=$2

ssh $USER@$HOST
cd repos/fridge-bridge/anual
make update-server
make update-docs

# update systemd services
sudo systemctl daemon-reload
