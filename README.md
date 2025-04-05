Simple Notes Backend using Ktor demnonstrating auth and database handling

- Ktor
- Kotin Serialization
- JWT Authentication
- MongoDB
- BCrypt
- Koin For dependency injection

# Apis Supported
- POST /register        -> Register User using username and password
- POST /login           -> Login using username and password
- GET /note/            -> Get All notes for logged in user
- GET /note/{id}        -> Get note by id beloging to logged in user
- POST /note/           -> Add Note 
- PUT /note/            -> Update Note
- DELETE /note/{id}     -> Delete note by id

# How to run

## **Set ENVIRONMENT VARIABLE**
- Edit Configuration -> Environment Variables
- set **ISSUER**
- set **MONGO_DB_URI**
- set **JWT_SECRET**
- set **AUDIENCE**

## Run
- Hit play on IDE

You can use [MongoDB Atlas](https://cloud.mongodb.com) free plan for testing the database integration.
