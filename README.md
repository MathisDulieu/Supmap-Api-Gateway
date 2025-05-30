# Api-Gateway

## Description
The Api-Gateway serves as the central entry point for the SUPMAP application's microservices architecture. It handles routing, authentication, authorization, and provides a unified API interface for clients while managing cross-cutting concerns like security and documentation.

## Features
- Centralized routing to microservices
- Authentication and authorization with JWT tokens
- OAuth2 integration with Google
- API documentation with Swagger/OpenAPI
- Cross-Origin Resource Sharing (CORS) configuration
- Request validation
- Rate limiting and circuit breaking
- File upload handling
- QR code generation for route sharing
- Monitoring and metrics collection

## Tech Stack
- Java 21
- Spring Boot 3.4.4
- Spring Security with JWT
- OAuth2 for social login
- MongoDB for configuration storage
- Elasticsearch for search capabilities
- Kafka for event messaging
- Cloudinary for image uploads
- Swagger/OpenAPI for API documentation
- Prometheus for metrics

## Dependencies
- shared-models: Common data models for the SUPMAP ecosystem
- database-utils: Database utility functions
- Spring Security for authentication and authorization
- JJWT for JWT token generation and validation
- WebFlux for reactive communication with microservices
- Cloudinary for profile image and media management
- Elasticsearch for search functionality
- OpenAPI for API documentation

## Configuration
The service can be configured via environment variables:

```yaml
supmap:
 properties:
   allowed-origins: http://localhost:3000,https://supmap.com
   database-name: api_gateway_db
   elasticsearch-password: your-password
   elasticsearch-url: http://elasticsearch:9200
   elasticsearch-username: elastic
   jwt-secret: your-secret-key
   kafka-bootstrap-servers: kafka:9092
   mongo-uri: mongodb://user:password@mongodb:27017/api_gateway_db
   frontend-base-url: https://supmap.com
   cloudinary-api-key: your-cloudinary-key
   cloudinary-cloud-name: your-cloud-name
   cloudinary-api-secret: your-api-secret
   qr-code-api-url: https://api.qrserver.com/v1/create-qr-code