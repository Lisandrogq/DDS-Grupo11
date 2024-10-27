### Instrucciones para importar colaboradores y contribuciones mediante CSV

Este documento proporciona las instrucciones necesarias para importar datos de colaboradores y sus contribuciones en un archivo CSV al sistema de la ONG. A continuación, se describen los pasos para realizar la importación, así como los requisitos y formato del archivo CSV.

#### Requisitos del Archivo CSV

El archivo CSV debe tener el siguiente formato y cumplir con las especificaciones a continuación:

| Campo                 | Formato  | Longitud Máx | Observación                                                                                       |
| --------------------- | -------- | ------------ | ------------------------------------------------------------------------------------------------- |
| Tipo Doc              | Texto    | 3            | Valores: LC (Libreta cívica), LE (Libreta de enrolamiento), DNI (Documento nacional de identidad) |
| Documento             | Numérico | 10           | Número de documento del colaborador                                                               |
| Nombre                | Texto    | 50           | Nombre del colaborador                                                                            |
| Apellido              | Texto    | 50           | Apellido del colaborador                                                                          |
| Mail                  | Texto    | 50           | Formato: usuario@dominio                                                                          |
| Fecha de colaboración | Fecha    | 10           | Formato: dd/MM/yyyy                                                                               |
| Forma de colaboración | Texto    | 22           | Valores: DINERO, DONACION_VIANDAS, REDISTRIBUCION_VIANDAS, ENTREGA_TARJETAS                       |
| Cantidad              | Numérico | 7            | Cantidad de la contribución                                                                       |

#### Formato del Archivo CSV

El archivo CSV debe seguir el siguiente formato de ejemplo:

```csv
Tipo Doc,Documento,Nombre,Apellido,Mail,Fecha de colaboración,Forma de colaboración,Cantidad
LC,123456,John,Doe,johndoe@example.com,15/06/2023,DINERO,100
LE,654321,Jane,Smith,janesmith@example.com,10/06/2023,DONACION_VIANDAS,0
DNI,112233,Jim,Beam,jimbeam@example.com,12/06/2023,REDISTRIBUCION_VIANDAS,50
LC,334455,Jill,Stark,jillstark@example.com,20/06/2023,ENTREGA_TARJETAS,0
LE,556677,Bob,Brown,bobbrown@example.com,18/06/2023,DINERO,200
```

#### Instrucciones para la Importación

1. **Preparar el Archivo CSV**:

   - Asegúrese de que el archivo CSV esté correctamente formateado según las especificaciones anteriores.
   - Verifique que todos los campos estén completos y que los datos sean válidos.

2. **Ubicar el Archivo CSV**:

   - Coloque el archivo CSV en el directorio de recursos del proyecto.

3. **Importar los Datos**:
   - Inicie la console interactiva de java: `./run.sh`
   - Cargue los datos: `app.bulkContributionsImport(<YOUR_FILE_NAME>);`

#### Notificación a Nuevos Colaboradores

Una vez procesado el archivo, se enviará un correo electrónico a aquellos colaboradores que no tenían usuario en el sistema previamente. El correo electrónico agradecerá el aporte realizado y brindará credenciales de acceso para que el colaborador pueda ingresar al sistema, confirmar sus datos y completar la información faltante.
