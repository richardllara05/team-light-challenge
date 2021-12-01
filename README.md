**Team LightFeather Challenge**
===========================
> Fun coding challenge for Team LightFeather. 
REST API built with Spring Framework.

<details open="open">
  <summary>Table of Contents</summary>
  <ul>
    <li>
        <a href="#getting-started"> Getting Started</a>
        <ul>
            <li>
                <a href="#requests"> Requests </a>
            </li>
            <li>
                <a href="#usage">  Usage </a>
            </li>
            <li> 
                <a href="#testing">  Testing</a>
            </li>
        </ul>
    </li>
  </ul>
</details>

<span id="getting-started">**Getting Started**</span>
----------------------------

This project uses docker-compose to build a Java Spring container.
```docker
docker-compose up -d
```
Running the above command launches a "java_spring" container. The image exposes port 8080 of the container. 

|   endpoints      | method |
|------------------|--------|
| /api/supervisors |  GET   |
| /api/submit      |  POST  |

> Once the container has **completely started**, perform requests to the following endpoints.

<span id="requests">**Requests**</span>
--------
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
<span id="#usage">**Usage**</span>
----------------------------------
<hr>

The following command starts the server.
```gradle
gradle bootRun
```
If you want to use the JAR file, run the following.
```bash
java -jar ./build/libs/team-lightfeather-challenge-0.0.1-SNAPSHOT.jar
```

<span id="#testing">**Testing**</span>
--------------------------------------
<hr>

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