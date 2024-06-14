## Anual

### Entrega 2
- [x] Diagrama de clases
- [x] Bocetos
- [x] Implementación modelos
- [x] Integrar los modelos en una clase app para permitir interactuar con los mismos
- [x] Implementación carga masiva
- [x] Implementación mapa (indagar sobre la implementación, me parece muy temprano hacerla ya... para mi es en el boceto por lo que ya lo tendríamos hecho)
- [x] Redactar bien las justificaciones del excel
- [x] Add sensor manager code
- [x] Add reward quantity logic
- [x] Add validation to the necessary fields to the csv loading
- [x] Add singleton to classes
- [ ] Refactorear timestamps en unix por Date object, en lugar de estar parseando?
### Correcciones/observaciones defensa 14/06
- [ ] Clase tarjeta: ¿por qué se agrega? ¿no hace referencia a algo físico?
- [ ] Método colaborar: hace referencia a algo muy físico. "contribución.contribuir" es medio raro debería ser algo como agegar contribución, un set
- [ ] Person in need y técnico: es polémico que esté separado de persona...
- [ ] Sensores: está extraño en el diagrama, puede que sea muy complejo pero puede que esté bien igual :) #revisar y justificar. ¿Por qué están los temp max y min en el sensor y no en la heladera? sacar last temp del sensor
- [ ] Heladera: en vez de tener dos sensor managers, implementar una colección de sensor managers
- [ ] Mail: importante, probar hacer envío de mail → sendgrid
- [ ] Mantener actualizado el diagrama de CdU
- [ ] Figma: habrá más roles en el sistema, tendrá que haber más bocetos
- [ ] RewardSystem: poner los coeficientes en las contribuciones (precalculado). patrón strategy?
- [ ] Rewards: agregarles a los contribuidores sus rewards
- [ ] DataImporter: deberían ser muchas más clases
- [ ] Paquete behavior: ta raro
- [ ] API: módulo separado del código (otro proyecto), diagrama de clases aparte, agregar casos de prueba
- [ ] CSV: hacer el archivo CSV y un archivo diciendo cómo cargar el CSV, diagrama de clases aparte, agregar casos de prueba
 
## General
- [x] Ejercicio Puertos
- [x] Ejercicio pokemon api
- [x] Test de password validator (quizá habría que agregar mas casos, pero la base está)
- [x] Requerimientos No Funcionales y Atributos de Calidad

