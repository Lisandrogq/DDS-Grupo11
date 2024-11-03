## How to deploy Fridge Bridge

To deploy Fridge Bridge you'll need to:

- [Install Ansible](https://docs.ansible.com/ansible/latest/installation_guide/intro_installation.html)
- Have a debian based server.

First, change the [json deploy configuration]() with the appropriate parameters, here is what the config looks like:

```json
{
  "user": "<USER>",
  "domain_name": "<DOMAIN_NAME",
  "postgres_db": "<POSTGRES_DB>",
  "postgres_user": "<POSTGRES_USER>",
  "postgres_password": "<POSTGRES_PASSWORD>",
  "rabbitmq_user": "<RABBITMQ_USER>",
  "rabbitmq_pass": "<RABBITMQ_PASS>"
}
```

> Notes:
>
> - `user`: refers to the host machine user.
> - `domain_name`: the domain that is pointing to the host machine, for example: `fridgebridge.simplecharity.com`.

Next, you need to add your host to ansible. For that add a new entry to `/etc/ansible/hosts/`, for example:

```conf
[fridge-bridge]
fridgebridge-1 ansible_host=<YOUR_HOST_IP>
```

Once you have modified the config file, from the project root run:

```shell
make deploy HOST=<ANSIBLE_HOST>
```

This will:

- Install all the basic tools, deps for building the project
- Start systemd services for postgres, rabbit, the java server and the docs.
- It will start a proxy server with nginx on ports `:80` and `:443`.

After that, it will get ssl certificates with `certbot`. This tool, will prompt for some parameters. In order for `certbot` to work, you need to make sure your domain is correctly pointing to your host.

Finally, check that the setup went well by going to your domain at:

- `https://<domain_name>`: you should see the fridge bridge client
- `https://<domain_name>/docs`: you should see the fridge bridge documentation

## Update your host

If you have already deployed fridge bridge, upgrading your host to the latest release is straightforward, just from the project root run:

```shell
make deploy-upgrade USER=<HOST_USER> HOST=<YOUR_HOST>
```

This will `ssh` into your host and restart the systemd-services which will trigger a recompilation.
