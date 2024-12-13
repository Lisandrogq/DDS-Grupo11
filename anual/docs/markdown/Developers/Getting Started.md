---
sidebar_position: 1
---

This will guide you in setting up FridgeBridge iny your local environment for developing/contributing.

## Dependencies

Before, make sure you have the following dependencies:

-   java: we suggest installing at least version 22 and the [openJDK version](https://openjdk.org/projects/jdk/22/)
-   [maven](https://maven.apache.org/): the package manager we are using
-   [docker engine](https://docs.docker.com/engine/install/) and [docker-compose plugin](https://docs.docker.com/compose/install/): we use it for quickly setup the rabbit and postgress instance and deploy the project in prod.

## How to run

1. Clone the project: `git clone https://github.com/Lisandrogq/DDS-Grupo11/tree/main`
2. cd into anual folder: `cd anual`
3. Setup the `.env` file (change it as you need): `make setup_env`
4. Start the server: `make start`
5. Run test with: `make test`
6. For all commands run: `make help`

**Adminer**
When developing you might want to see the current status of the database. You could enter the into the postgres container shell and use the cli tool by running:

```shell
docker exec -it fridge-bridge-postgres-1 sh
```

or use the adminer GUI though a browser by opening: http://localhost:8080/?pgsql=postgres&username=postgres&db=postgres&ns=public

### Docs

To run the documentation front-end run:

```shell
make docs_install
make docs_dev
```

### Metrics

To start metrics services run:

```shell
make start_metrics
```

### Mocking db

In testing environments, you might want to fill your db with mock data to run different tests. For that you need to:

1. Run the project: `make start`, this will start the necessary containers for the db and create the tables with hibernate.
2. Then run: `make mock-db`
3. Check that data has been inserted.
