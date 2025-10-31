# Book Management Service

A secure Spring Boot application for managing books in financial systems.

## Access Links

- **Swagger UI**: [http://localhost:8080/api/swagger-ui.html](http://localhost:8080/api/swagger-ui.html)
- **H2 Console**: [http://localhost:8080/api/h2-console](http://localhost:8080/api/h2-console)
  - JDBC URL: `jdbc:h2:mem:bookdb`
  - Username: `sa`
  - Password: (leave empty)

## API Documentation

The API follows OpenAPI 3.0 specification and is documented via Swagger. Key endpoints:

- `POST /api/v1/books` - Create a new book
- `GET /api/v1/books` - List all books
- `GET /api/v1/books/{id}` - Get book by ID
- `PUT /api/v1/books/{id}` - Update book
- `DELETE /api/v1/books/{id}` - Delete book

## Development

### Requirements
- Java 17+
- Maven 3.8+

### Running the Application
```bash
mvn spring-boot:run
```

### Testing
Run HTTP tests from `src/test/http/book-api-test.http`

## Configuration
See `application.yml` for:
- Server port and context path
- Database settings
- Logging configuration
- Swagger settings

## Security Considerations
- All endpoints require authentication (configured via Spring Security)
- Audit logging enabled for all modifications
- Input validation on all requests