Team LightFeather Challenge
===========================
> Fun coding challenge for Team LightFeather. 
REST API built with Spring Framework.

Getting Started
---------------

This project uses docker-compose to build a Java Spring container.
```docker
docker-compose up -d
```
Running the above command launches a "java_spring" container. The image exposes port 8080 of the container. As soon as the container launches, a jar file is built in the build directory and the Spring server starts up. 

|   endpoints      | method |
|------------------|--------|
| /api/supervisors |  GET   |
| /api/submit      |  POST  |

> Once the container has **completely started**, perform requests to the following endpoints.

Requests
------------
<hr>

## POST Endpoint

JSON payload format
```json
{
    "user-firstName": "Harry",
    "user-lastName": "Pita",
    "user-email": "hp@gmail.com",
    "user-phoneNumber": "123-344-6563",
    "supervisor": {
        "id": "1",
        "phone": "204-798-9969",
        "jurisdiction": "u",
        "identificationNumber": "d4900a18-a304-42c6-a8e5-a6c8c3f17bc0",
        "firstName": "Karson",
        "lastName": "Olson"
    }
    
}
```

Testing
-------
The following command runs the test scripts.
```gradle
gradle test --info
```

Features
--------
* Docker Support
* CircleCI Support
* JUnit Testing
* Exposed Port 8080
* Pre-recieve hook
