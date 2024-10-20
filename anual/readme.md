## Anual

anual assignment.

### Resources

- [Diagrams](https://app.diagrams.net/?libs=general;uml#G1o_ooQYoGarYq9FF1gDRubEYKmAPNF90K#%7B%22pageId%22%3A%22C5RBs43oDa-KdzZeNtuy%22%7D)
- [Justifications - Excel](https://docs.google.com/spreadsheets/d/1fUp0v8w6_35XXzrJLJNwBvbo_W9sJLq9swMP_iFxI84/edit#gid=0)
- [Figma](https://www.figma.com/file/l4YH5M21JTrqkBAEDC0iSx/Untitled?type=design&node-id=0%3A1&mode=design&t=dpcaHSFlc9CnMcil-1)
- [Assignment](https://suriweb.com.ar/archivos/general/DDS-TPA-2024.pdf)
- [mockAPI](https://mockapi.io/projects/665264aa813d78e6d6d56913)

### How to run

Before, make sure you have the following dependencies:

- java 22: we suggest installing at least version 22 and the [openJDK version](https://openjdk.org/projects/jdk/22/)
- [maven](https://maven.apache.org/): the package manager we are using
- [docker engine](https://docs.docker.com/engine/install/) and [docker-compose plugin](https://docs.docker.com/compose/install/): we use it for quickly setup the rabbit and postgress instance and deploy the project in prod.

1. Clone the project: `git clone https://github.com/Lisandrogq/DDS-Grupo11/tree/main`
2. cd into this folder: `cd anual`
3. Create a `.env` [here](./src/main/resources/) and the following fields:
   - SENDGRID_API_KEY=<YOUR_KEY_HERE>
   - COMPANY_MAIL=<YOUR_COMPANY_MAIL_HERE>
4. Run: `make start_and_compile`
5. Run test with: `make test`
6. For all commands run: `make help`

**Adminer**
When developing you might want to see the current status of the database. You could enter the into the postgres container shell and use the cli tool by running:

```shell
docker exec -it fridge-bridge-postgres-1 sh
```

or use the adminer GUI though a browser by opening: http://localhost:8080/?pgsql=postgres&username=postgres&db=postgres&ns=public

### Docs

- [Importing data through csv file](./docs/csv_import.md)
