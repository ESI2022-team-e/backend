# ESI2022 Team E backend

![example branch parameter](https://github.com/ESI2022-team-e/backend/actions/workflows/build.yml/badge.svg?branch=main)

The source code of Enterprise System Integration 2022 spring project of Team E:

* Lana Botchorishvili
* Karoliine Holter
* Carolin Kirotar
* Marta Napa.

## Getting started

For the first time:

1. Clone the project
2. Make a Postgres database named "project" in your computer (for example following the steps below)
    * Open pgAdmin
    * From the left column, right-click on “Databases”
    * Click “create”
    * Create a database with the name _project_
3. Open the project in IntelliJ (or VSCode)
4. Open file `application.properties` and on line 9 change `spring.jpa.hibernate.ddl-auto` from `update` to `create`
5. Run the project (there will be an error running it the first time)
6. Stop the project
7. Change `spring.jpa.hibernate.ddl-auto` back to `update`
8. Run the project again (should be OK)

Try it out:

* Open http://localhost:8080/api/cars in the browser (should show the list of cars)
