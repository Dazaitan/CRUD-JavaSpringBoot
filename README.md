##Personal Finance Tracker Application

Objetivo: Desarrollar una aplicación completa que ayude a los usuarios a administrar y rastrear sus actividades financieras.

## Requisitos Previos Asegúrate de tener las siguientes herramientas instaladas en tu máquina: 
- Java 17
- Gradle Groovy
- MySQL

##Creacion de base de datos MySQL
```bash
  CREATE DATABASE transactions;
  USE transactions;
  
  CREATE TABLE Transaction (
  ID INT PRIMARY KEY AUTO_INCREMENT,
  Type ENUM('Income', 'Expense'),
  Amount DECIMAL(10,2) NOT NULL,
  Category VARCHAR(50),
  Date DATE,
  Description TEXT
  );
```
##Instalacion.
1. Clonar repositorio
  ```bash
  git clone https://github.com/Dazaitan/CRUD-JavaSpringBoot
  ```
2. Navega al directorio del proyecto:
   ```bash
   cd proyecto-api
   ```
3. Configura las variables de entorno en `application.properties`:
   ```properties
    spring.application.name=transaction
    spring.datasource.url=jdbc:mysql://localhost:3306/junior_transaction
    spring.datasource.username=root
    spring.datasource.password=T5j2%7uB8kL&
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
   ```
##Ejecucion
En el directorio del proyecto ejecutar el siguiente comando
```bash 
./gradlew bootRun
```
La aplicación estará disponible en `http://localhost:8080`

##EndPoints
- `POST /transactions` - Crear una nueva transaccion financiera
- `GET /transactions` - Obtener todas las transacciones financieras.
- `GET /transactions/{id}` - Obtener detalles de una transaccion por su ID.
- `PUT /transactions/{id}` - Actualizar una transaccion por su ID.
- `DELETE /transactions/{id}` - Eliminar una transaccion por su ID.
- `GET /transactions/summary` - Recupere un resumen de datos financieros, que podría incluir ingresos totales, gastos totales y saldo neto para un período de tiempo específico.
- `GET /transactions/by-category` - Recupere transacciones filtradas por categorías específicas, lo que podría ser útil para categorizar los hábitos de gasto.
- `GET /transactions/by-date` - Recupere transacciones dentro de un rango de fechas específico, lo que ayuda en la elaboración de presupuestos y el seguimiento financiero a lo largo de períodos.
- `GET /categories` - Recupere todas las categorías disponibles para las transacciones para garantizar la coherencia en la categorización.
