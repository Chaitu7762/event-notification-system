# Event Notification System

## Tech Stack
- Java 17
- Spring Boot 3
- Maven
- Docker

## Features
- REST API for event submission
- Separate FIFO queues per event type
- Asynchronous processing
- Random failure simulation
- Callback mechanism
- Graceful shutdown
- Unit testing with JUnit & Mockito

## Run Locally

mvn clean package
java -jar target/event-0.0.1-SNAPSHOT.jar

## Run With Docker

docker build -t event-system .
docker run -p 8080:8080 event-system
