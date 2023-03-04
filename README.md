# user-excercise

- Java AdoptOpenJDK\jdk8u282-b08
- Gradle gradle-6.9.4
- Spring boot 2.6.14

#### Diagramas
En carpeta diagrams:
- Diagrama de Clases
- Diagramas de Paquetes
- Diagrama de Secuencias

### Base de Datos
El resultado es almacenado en una base de datos relacional embebida (H2), cuya tabla debe ser:

*Schema*: exercise
*Tabla*: tbl_user

|nombre campo|observaciones|
|---|---|
|id         |uuid primary key|
|name       |varchar(250) not null|
|email      |varchar(100) not null|
|password   |varchar(16)  not null|
|token      |varchar(200) not null|
|is_active  |boolean      default true|
|created_at |timestamp|    
|updated_at |timestamp|    
|modified   |timestamp|    

*Schema*: exercise
*Tabla*: user_phone

|nombre campo| observaciones|
|---|---|
|id           | uuid primary key|
|id_user      | uuid      not null|
|phone_number | integer   not null|
|city_code    | integer   not null|
|country_code | integer   not null|
|created_at   | timestamp not null default current_timestamp()|
|updated_at   | timestamp not null default current_timestamp()|
*FOREIGN KEY (id_user) REFERENCES EXERCICE.TBL_USER (id)*

#### Acceso a BD
Vía browser http://localhost:8099/user/h2-console
- JDBC URL: `jdbc:h2:mem:exercise`
- User Name: `sa`
- Password: `password`

### Ejecución

#### Variables de ambiente
* DB_DIALECT=H2Dialect
* DB_DRIVER=org.h2.Driver
* DB_PASSWORD=password
* DB_URL=jdbc:h2:mem:exercise
* DB_USER=sa
* LOG_LEVEL=INFO
* LOG_LEVEL_SQL=INFO

Las variables están en el archivo .env

#### Docker
Ejecutar comando: 

`docker-compose -f docker-compose.yml up -d --build`

#### Gradle
```shell script
source .env
gradle clean build bootRun
```

#### Documentación API

Para la ejecución el flujo es el siguiente:
- Sign-up para el registro de nuevo usuario, por defecto no hay ningún usuario en la BD.
- Ejecutar login para obtener token de la respuesta
- Ejecutar update o get se debe agregar token Bearer en Authorization en Header

Puede utilizar Swagger (http://localhost:8098/user/swagger-ui/index.html) 
o un cliente API Rest como Postman o Insomnia

#### Contratos
##### IN
###### Endpoint sign-up / update
- Obligatorios: email, password
- Opcional: phones *en caso de enviarlo todos los campos son obligatorios*
- Validaciones: configurables en el archivo application.properties
  - email: formato email correcto user@micompania.com
  - contraseña: minimo 1 letra mayuscula, minusculas, 2 numeros, 1 un simbolo [@#$%^&-+=()]
```json
{
  "name": "Juan Rodriguez",
  "email": "juan@rodriguezorg.cl",
  "password": "ssT4S@Aww4",
  "phones": [
    {
      "number": "1234567",
      "citycode": "1",
      "countrycode": "57"
    }
  ]
}
```
###### Endpoint login
```json
{
	"email": "juan@rodriguezdorg.cl",
	"password": "2A@aaa3to"
}
```
##### OUT
###### Endpoint sign-up (token es el password encriptado)
````json
{
	"id": "0ea86112-7439-4706-a13b-a6827eeac747",
	"isactive": true,
	"token": "$2a$10$jOaQ5c.YwOEIoQrlkGF9/ukDdPlwnuaypCERB7O95kztMSh9VscIS",
	"created": "sábado, marzo 04, 2023 01:46:06.777 PM",
	"modified": "sábado, marzo 04, 2023 01:46:06.777 PM",
	"last_login": "sábado, marzo 04, 2023 01:46:06.777 PM"
}
````
###### Endpoint sign-up / update
```json
{
  "id": "ba39e9a6-f88d-4a59-adbb-eeb448ba0ca7",
  "isactive": true,
  "token": "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJqdWFuQHJvZHJpZ3VlemRvcmcuY2wiLCJleHAiOjE2ODA1Mzk0NTcsInBhc3N3b3JkIjoiJDJhJDEwJGR4eFpGMmp1ekhGazlFeU1KSEouT092ZDRucmpaT25uWlNnUkZnZzh4aUJDcDh2WjNSd1R1IiwidXNlck5hbWUiOiJKdWFuIFJvZHJpZ3VleiIsInVzZXJDb2RlIjoiYmEzOWU5YTYtZjg4ZC00YTU5LWFkYmItZWViNDQ4YmEwY2E3In0.Oo_fGY88TthJpOjmKrz-JPmahWVjFAVmZkolrEtLnwlgptzCeQAO5L4iVvtih5Qx",
  "created": "sábado, marzo 04, 2023 01:30:53.640 PM",
  "modified": "sábado, marzo 04, 2023 01:31:27.908 PM",
  "last_login": "sábado, marzo 04, 2023 01:30:57.499 PM"
}
```
###### Endpoint login
```json
{
  "id": "0ea86112-7439-4706-a13b-a6827eeac747",
  "isactive": true,
  "token": "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJqdWFuQHJvZHJpZ3VlemRvcmcuY2wiLCJleHAiOjE2ODA1NDAzNzIsInBhc3N3b3JkIjoiJDJhJDEwJHY1YS5sUFR2L04zRktpZlA5Lk1rQS4zRDh2L0txVXJpbGxDMjJodjZDQ0RlNDcxaklFSmNpIiwidXNlck5hbWUiOiJKdWFuIFJvZHJpZ3VleiIsInVzZXJDb2RlIjoiMGVhODYxMTItNzQzOS00NzA2LWExM2ItYTY4MjdlZWFjNzQ3In0.TGYc_WiOjYsterVllSUWatHUZjiKthj6ftzrbVD6gcBonbvqa1pvuY9Tt0U2mq_Z",
  "created": "sábado, marzo 04, 2023 01:46:06.777 PM",
  "modified": "sábado, marzo 04, 2023 01:46:06.777 PM",
  "last_login": "sábado, marzo 04, 2023 01:46:12.121 PM"
}
```
###### Endpoint get
```json
{
  "phones": [
    {
      "number": 1234567,
      "citycode": 57,
      "countrycode": 57
    }
  ],
  "id": "0ea86112-7439-4706-a13b-a6827eeac747",
  "isactive": true,
  "token": "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJqdWFuQHJvZHJpZ3VlemRvcmcuY2wiLCJleHAiOjE2ODA1NDAzNzIsInBhc3N3b3JkIjoiJDJhJDEwJHY1YS5sUFR2L04zRktpZlA5Lk1rQS4zRDh2L0txVXJpbGxDMjJodjZDQ0RlNDcxaklFSmNpIiwidXNlck5hbWUiOiJKdWFuIFJvZHJpZ3VleiIsInVzZXJDb2RlIjoiMGVhODYxMTItNzQzOS00NzA2LWExM2ItYTY4MjdlZWFjNzQ3In0.TGYc_WiOjYsterVllSUWatHUZjiKthj6ftzrbVD6gcBonbvqa1pvuY9Tt0U2mq_Z",
  "created": "sábado, marzo 04, 2023 01:46:06.777 PM",
  "modified": "sábado, marzo 04, 2023 01:46:06.777 PM",
  "last_login": "sábado, marzo 04, 2023 01:46:12.121 PM",
  "name": "Juan Rodriguez",
  "email": "juan@rodriguezdorg.cl"
}
```

###### Error
```json
{
  "errors": [
    {
      "timestamp": "2020-10-04 22:45:05",
      "status": 500,
      "title": "Internal Server Error",
      "detail": "[Error] El correo ya está registrado",
      "source": "uri=/user/sign-up"
    }
  ]
}
```