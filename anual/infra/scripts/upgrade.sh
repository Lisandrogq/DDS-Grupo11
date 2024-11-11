HOST=$1

ssh $HOST -t 'cd repos/fridge-bridge/anual && git pull && sudo systemctl restart java_server && sudo systemctl restart docs'
