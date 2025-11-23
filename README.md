# Multi-tenant Test Data API - Full Project

This project contains both Service A (Bulk Sync) and Service B (On-Demand Allocator).

See src/main/resources/queries for SQL examples. Update application.yml with your Sybase connection strings and Mongo details.

Build & run:
1. Start Mongo: docker-compose up -d
2. Add Sybase JDBC jar to local maven or lib/ directory
3. mvn -DskipTests clean package
4. java -jar target/multi-tenant-testdata-api-1.0.0.jar

Endpoints:
- POST /sync/{env}  -> manual sync
- POST /alloc/allocate?env=env1&category=...&ownerId=...
- POST /alloc/release?env=env1&id=...&ownerId=...
- GET /alloc/{env}/{id}
