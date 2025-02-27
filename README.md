#Spring Boot User Registration API with MySQL & Docker

This project provides a Spring Boot API for user registration, authentication, and admin functionalities. The application is Dockerized and uses MySQL as the database.

#Features

    - User Registration (Stores IP address & country)

    - JWT Authentication

    - Admin Role-Based Access Control

    - Register User

    - Swagger API Documentation

    - JUnit Tests for Services

    - Docker Support (Spring Boot + MySQL)

#Technologies Used

    - Spring Boot 3

    - Spring Security

    - JWT Authentication
    
    - MySQL 8

    - JPA (Hibernate)

    - Docker & Docker Compose

    - JUnit 5

    - Swagger for API Docs

Setup & Installation

    1. Clone the Repository

        git clone https://github.com/jerone-as/task.git

        cd task

    2. Configure Database and Security in application.properties

        spring.datasource.url=jdbc:mysql://{hostname}:3306/spring_task

        spring.datasource.username={db_username}

        spring.datasource.password={db_password}

        spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

        spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect

        spring.jpa.hibernate.ddl-auto=update

        jwt.secret.key=mySecretKeyForJwtMySecretKeyForJwt

        #1h = 3600s and 3600*1000 = 3600000 milliseconds
        
        jwt.expiration.time=3600000 

    3. Build & Run Docker Containers

        mvn clean package

        docker image build -t task:latest .

        docker run -p 8080:8080 task:latest

    5. Open API Documentation (Swagger)

        http://localhost:8080/swagger-ui/index.html

API Endpoints

    Authentication

        POST /api/auth/register -> Register a new user

        POST /api/auth/login -> Login & get JWT token(Token only for admin user)

    Admin Endpoints (Restricted)
    
        GET /api/admin/users -> Get all registered users

        DELETE /api/admin/delete/{email} -> Delete user by email

Run JUnit Tests

    mvn test