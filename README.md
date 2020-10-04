# user-excercice

### Base de Datos
El resultado es almacenado en una base de datos relacional embebida (H2), cuya tabla debe ser:

*Schema*: exercise
*Tabla*: tbl_user

|nombre campo|observaciones
|---|---|
|id         |uuid         not null DEFAULT RANDOM_UUID() primary key
|name       |varchar(250) not null
|email      |varchar(100) not null
|password   |varchar(16)  not null
|token      |varchar(200) not null
|is_active  |boolean      not null default true
|created_at |timestamp    not null default current_timestamp()
|updated_at |timestamp    not null default current_timestamp()
|modified   |timestamp    

*Schema*: exercise
*Tabla*: user_phone

|nombre campo| observaciones|
|---|---|
|id           | uuid primary key,
|id_user      | uuid      not null,
|phone_number | integer   not null,
|city_code    | integer   not null,
|country_code | integer   not null,
|created_at   | timestamp not null default current_timestamp(),
|updated_at   | timestamp not null default current_timestamp(),
*FOREIGN KEY (id_user) REFERENCES EXERCICE.TBL_USER (id)*

#### Acceso a BD
Vía browser http://localhost:8099/user/h2-console
- JDBC URL: `jdbc:h2:mem:exercise`
- User Name: `sa`
- Password: `password`

### Ejecución

#### Docker
Ejecutar comando: 

`docker-compose -f docker-compose.yml up -d --build`

#### Gradle
```shell script
source .env
gradle clean build bootRun
```

#### Rest Client
- Importar archivo restClientUser.json a un cliente Rest (Insomnia / Postman)
- Ejecutar sig-up
- Ejecutar login y copiar token de la respuesta
- Cambiar token en Authorization en Header
- Ejecutar sign-in o update 

#### Contratos
##### IN
```json
{
  "name": "Juan Rodriguez", //obligatorio
  "email": "juan@rodriguezorg.cl", //obligatorio
  "password": "ssT4S@Aww4", //obligatorio
  "phones": [ //opcional, en caso de enviarlo todos los campos son obligatorios
    {
      "number": "1234567",
      "citycode": "1",
      "countrycode": "57"
    }
  ]
}
```

##### OUT
###### Endpoint sign-in / update
```json
{
  "id": "ba9ac26d-af97-46f4-a4ec-711a99bc4e04",
  "isactive": true,
  "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqdWFuQHJvZHJpZ3Vlem9yZy5jbCIsImV4cCI6MTYwMTc3OTUwNX0.5Lm353B6_9rpgNd19am7lky9WMcmvDtictPlbWmcvJcUoGD7nQik-Dz_uXb_ymnVq_F7Z_2BV8GmXitQUotEcQ",
  "created": "sábado, octubre 03, 2020 11:29:59.383 PM",
  "modified": "sábado, octubre 03, 2020 11:30:52.252 PM",
  "last_login": "sábado, octubre 03, 2020 11:30:05.316 PM"
}
```
###### Endpoint login
```json
{
  "user": "juan@rodriguezorg.cl",
  "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqdWFuQHJvZHJpZ3Vlem9yZy5jbCIsImV4cCI6MTYwMTc3OTUwNX0.5Lm353B6_9rpgNd19am7lky9WMcmvDtictPlbWmcvJcUoGD7nQik-Dz_uXb_ymnVq_F7Z_2BV8GmXitQUotEcQ"
}
```
###### Endpoint sign-in
```json
{
  "phones": [
    {
      "number": 1234567,
      "citycode": 7,
      "countrycode": 7
    },
    {
      "number": 999999999,
      "citycode": 56,
      "countrycode": 56
    }
  ],
  "name": "Juan Rodriguez",
  "email": "juan@rodriguezorg.cl",
  "password": "ssT4S@Aww4",
  "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqdWFuQHJvZHJpZ3Vlem9yZy5jbCIsImV4cCI6MTYwMTc3OTUwNX0.5Lm353B6_9rpgNd19am7lky9WMcmvDtictPlbWmcvJcUoGD7nQik-Dz_uXb_ymnVq_F7Z_2BV8GmXitQUotEcQ"
}
```