#!/bin/bash
HOST=$1

# Setup certificates using certbot
ssh debian@$HOST

# Install certbot with snap
sudo snap install snapd
sudo snap install --classic certbot
sudo ln -s /snap/bin/certbot /usr/bin/certbot

# Generate certificates
sudo certbot certonly --nginx
sudo certbot renew --dry-run
sudo systemctl restart nginx
