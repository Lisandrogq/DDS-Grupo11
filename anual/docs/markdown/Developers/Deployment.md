## How to deploy Fridge Bridge

To deploy Fridge Bridge you'll need to:

- [Install Ansible](https://docs.ansible.com/ansible/latest/installation_guide/intro_installation.html)
- [jq](https://jqlang.github.io/jq/download/)
- Have a debian based server.

First, you need to add your host to ansible. For that add a new entry to `/etc/ansible/hosts/`:

```conf
[GROUP]
<NAME> ansible_host=<YOUR_HOST_IP>
```

For example:

```conf
[fridge-bridge]
fridge-bridge-1 ansible_host=<YOUR_HOST_IP>
```

Next, change the [json deploy configuration](https://vscode.dev/github/Lisandrogq/DDS-Grupo11/blob/deploy-infra/anual/infra/scripts/deploy-conf.json#L1-L13) with the appropriate parameters, here is what the config looks like:

```json
{
  "host": "fridge-bridge-aws",
  "host_ansible": "fridgebridge-1",
  "user": "admin",
  "domain_name": "fridgebridge.simplecharity.com",
  "postgres_url": "jdbc:postgresql://localhost:5432/fridgebridge",
  "postgres_db": "fridgebridge",
  "postgres_user": "fridgebridge",
  "postgres_password": "YMMetjdIClkVKIf",
  "rabbitmq_user": "fridgebridge",
  "rabbitmq_pass": "YMMetjdIClkVKIf"
}
```

> Notes:
>
> - `host`: refers to the hostname of the host (what you used when doing `ssh user@hostname`).
> - `host_ansible`: refers to the ansible host name you have set in the previous step.
> - `user`: refers to the host machine user.
> - `domain_name`: the domain that is pointing to the host machine, for example: `fridgebridge.simplecharity.com`.

Once you have modified the config file, from the project root run:

```shell
make deploy
```

This will:

- Install all the basic tools, deps for building the project
- Start systemd services for postgres, rabbit, the java server and the docs.
- It will start a proxy server with nginx on ports `:80` and `:443`.

After that, it will get ssl certificates with `certbot`. This tool, will prompt for some parameters. In order for `certbot` to work, you need to make sure your domain is correctly pointing to your host.

Finally, check that the setup went well by going to your domain at:

- `https://<domain_name>`: you should see the fridge bridge client
- `https://<domain_name>/docs`: you should see the fridge bridge documentation

If anything goes wrong, you can re-run the target or only the ones that failed.

## Update your host

If you have already deployed fridge bridge, upgrading your host to the latest release is straightforward, just from the project root run:

```shell
make deploy-upgrade USER=<HOST_USER> HOST=<YOUR_HOST>
```

This will `ssh` into your host and restart the systemd-services which will trigger a recompilation.
