# Book Store APIs

### Project setup

- We have `Book` entity with `manyToMany` mappings to `Author` and `Category`.
- Have used Spring JPA to bind these entities with underlying tables
- `BookStore` is the Spring repository for the REST API endpoints
- Have used `CrudRepository` to facilitate common CRUD operations and added few `JPQL` statements for custom querying.
- For dev env, Have used `H2` as a data source.
- When the application boots, Spring executes [schema.sql](src/main/resources/schema.sql) to create the tables
  and [data.sql](src/main/resources/data.sql) to populate the tables with few records.

### To run this in dev setup:

- Open a terminal on the project folder and run the below command:

```bash
java -jar build/libs/book.store-dev.jar
```

- To regenerate this executable jar, use the below gradle command:

```bash
gradlew bootJar
```

- To test out the available end-points, visit http://localhost:8080/swagger-ui/index.html when the application is
  running