# Custom Keycloak HTTP Listener

## Overview
This project extends Keycloak by adding a custom HTTP Listener. The listener intercepts HTTP requests to provide additional logging, metrics collection, or to implement custom security measures.

## Prerequisites
Before you build and deploy the custom listener, you need to have the following installed:
- JDK 11 or later
- Maven 3.6 or later

## Building the Project
To build the project, run the following command from the root of the repository:

```bash
mvn clean install

```

## Start the keycloak server
```bash
docker-compose up

```
