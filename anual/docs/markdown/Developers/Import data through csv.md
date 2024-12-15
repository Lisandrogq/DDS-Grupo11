#### Instructions for Importing Contributors and Contributions via CSV

This document provides the necessary instructions for importing contributor data and their contributions from a CSV file into the NGO system. The following sections outline the steps to perform the import, as well as the requirements and format of the CSV file.

#### CSV File Requirements

The CSV file must have the following format and meet the specifications below:

| Field             | Format  | Max Length | Notes                                                                                 |
| ----------------- | ------- | ---------- | ------------------------------------------------------------------------------------- |
| Doc Type          | Text    | 3          | Values: LC (Civic Booklet), LE (Enrollment Booklet), DNI (National Identity Document) |
| Document          | Numeric | 10         | Contributor's document number                                                         |
| First Name        | Text    | 50         | Contributor's first name                                                              |
| Last Name         | Text    | 50         | Contributor's last name                                                               |
| Email             | Text    | 50         | Format: user@domain                                                                   |
| Contribution Date | Date    | 10         | Format: dd/MM/yyyy                                                                    |
| Contribution Type | Text    | 22         | Values: MONEY, FOOD_DONATION, FOOD_REDISTRIBUTION, CARD_DELIVERY                      |
| Amount            | Numeric | 7          | Contribution amount                                                                   |

#### CSV File Format

The CSV file must follow this sample format:

```csv
Tipo Doc,Documento,Nombre,Apellido,Mail,Fecha de colaboración,Forma de colaboración,Cantidad
LC,123456,John,Doe,johndoe@example.com,15/06/2023,DINERO,100
LE,654321,Jane,Smith,janesmith@example.com,10/06/2023,DONACION_VIANDAS,0
DNI,112233,Jim,Beam,jimbeam@example.com,12/06/2023,REDISTRIBUCION_VIANDAS,50
LC,334455,Jill,Stark,jillstark@example.com,20/06/2023,ENTREGA_TARJETAS,0
LE,556677,Bob,Brown,bobbrown@example.com,18/06/2023,DINERO,200
```

#### Import Instructions

1. **Prepare the CSV File:**

- Ensure the CSV file is correctly formatted according to the specifications above.
- Verify that all fields are complete and that the data is valid.

2. **Log in as admin:**

- Log in with an admin account at [fridgebridge](https://fridgebridge.simplcharity.com/register/login)

3. **Import the Data:**

- Upload the `csv` file under the `Import Data` module.

#### Notification to New Contributors

Once the file is processed, an email will be sent to contributors who did not previously have a user account in the system. The email will thank them for their contribution and provide login credentials so they can access the system, confirm their data, and complete any missing information.
