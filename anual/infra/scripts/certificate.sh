#!/bin/bash
USER=$1
HOST=$2

# Setup certificates using certbot
ssh $USER@$HOST

# Install certbot with snap
sudo snap install snapd
sudo snap install --classic certbot
sudo ln -s /snap/bin/certbot /usr/bin/certbot

# Generate certificates
sudo certbot certonly --nginx
sudo certbot renew --dry-run
sudo systemctl restart nginx
