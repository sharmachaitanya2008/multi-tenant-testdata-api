Multi-tenant Test Data Service (sample)

Instructions:
1) Add Sybase JDBC driver to your local maven repo or classpath.
2) Edit src/main/resources/application.yml to point to your Sybase DBs and change API key.
3) Start Redis: docker-compose up -d
4) Build: mvn -DskipTests clean package
5) Run: java -jar target/multi-tenant-testdata-api-1.0.0.jar

Endpoints (X-API-KEY header required):
POST /data/allocate?env=env1&type=user
POST /data/release/{id}?env=env1
