# Spring kafka mongodb - sample project

![](https://travis-ci.com/viktorpenelski/spring-kafka-mongodb-demo.svg?branch=master)

The following project implements a simple data pipeline, utilizing SpringBoot, Kafka and MongoDB.

Springboot's embedded Tomcat is used to serve REST endpoints.


### Quick glance over the available flows:

![](media/flows.jpg)

*Generated using [draw.io](https://www.draw.io/), import [`media/skmd.drawio`](media/skmd.drawio) 
in draw.io to modify*

# Run locally

Dependencies:
- docker
- docker-compose
- java11

In order to run this project locally with all of its dependencies in docker, 
first build the artifact and then use the provided docker-compose.yml:

```
./gradlew clean build docker
docker-compose up
```

this will create a local image `com.github.viktorpenelski/spring-kafka-mongo-demo`
and bring it up along all of the dependencies (zoo, kafka, mongo).

The service will be available on `localhost:8080`


---

# Dev locally

Dependencies:
- docker
- docker-compose
- java11

In order to run only the dependencies (zoo, kafka, mongo) in docker, while
developing the app, you can use the alternative docker-compose:

```
docker-compose -f docker-compose-dependencies-only.yml up
```

After that you can run application from your IDE, or using:

```
./gradlew clean build
java -jar build/libs/*.jar
```

---

# API usage

### Enqueue a request:

`/jsonstore/enqueue` endpoint accepts any valid JSON as payload. Upon successful
submission, [HttpStatus 202 - Accepted](https://tools.ietf.org/html/rfc7231#page-52)
is returned with an `id` and `href` where the resource is expected to be
available at a later point. 

Note that enqueueing a request results in a REST resource almost immediately, but
since it is an asynchronous process served by a messaging queue, no guarantee 
is provided for when that will happen.


```
POST {{url}}:{{port}}/jsonstore/enqueue
```
With any valid JSON as body.

Example:

```
curl -X POST \
  http://localhost:8080/jsonstore/enqueue \
  -H 'content-type: application/json' \
  -d '{ "someKey": "some value" }'
```

Expected response:

```
Status: 202 Accepted
Body:
{
    "message": "Successfully enqueued the payload. Resource will be available shortly.",
    "id": "d3269c50-3525-4725-b0c0-f9c8598daae6",
    "href": "/jsonstore/d3269c50-3525-4725-b0c0-f9c8598daae6"
}
```

### Retrieve a resource

Get a single REST resource previously enqueued via the enqueue endpoint.

`GET {{url}}:{{port}}/jsonstore/{id}`

Example:
```
curl localhost:8080/jsonstore/d3269c50-3525-4725-b0c0-f9c8598daae6
```
Response:
```
{
  "payload" : {
    "someKay" : "some value"
  },
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/jsonstore/d3269c50-3525-4725-b0c0-f9c8598daae6"
    },
    "jsonstoreRecord" : {
      "href" : "http://localhost:8080/jsonstore/d3269c50-3525-4725-b0c0-f9c8598daae6"
    }
  }
}
```

### Retrieve all resources

Get all REST resources previously enqueued via the enqueue endpoint.

The endpoint supports client-requested pagination via query parameters:
- `page` - which page to start the current request form. First page is 0. 0 by default.
- `size` - page size. Each page will contain that many resources. 10000 by default.

```
GET {{url}}:{{port}}/jsonstore{&page,size}
```

Example:
```
curl localhost:8080/jsonstore
```
Response:
```
{
  "_embedded" : {
    "jsonstoreRecords" : [ {
      "payload" : {
        "someKay" : "some value"
      },
      "_links" : {
        "self" : {
          "href" : "http://localhost:8080/jsonstore/d3269c50-3525-4725-b0c0-f9c8598daae6"
        },
        "jsonstoreRecord" : {
          "href" : "http://localhost:8080/jsonstore/d3269c50-3525-4725-b0c0-f9c8598daae6"
        }
      }
    }, {
      "payload" : {
        "someKay" : "some value"
      },
      "_links" : {
        "self" : {
          "href" : "http://localhost:8080/jsonstore/bbc3dd1f-b99c-4de7-ae45-ceca3b34d866"
        },
        "jsonstoreRecord" : {
          "href" : "http://localhost:8080/jsonstore/bbc3dd1f-b99c-4de7-ae45-ceca3b34d866"
        }
      }
    } ]
  },
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/jsonstore{?page,size,sort}",
      "templated" : true
    },
    "profile" : {
      "href" : "http://localhost:8080/profile/jsonstore"
    }
  },
  "page" : {
    "size" : 10000,
    "totalElements" : 2,
    "totalPages" : 1,
    "number" : 0
  }
```

---

### Integration tests

The app contains an integration test suite. 

Currently it attempts to:

1. Create a resource via POST `/jsonstore/enqueue`
    - fails on unreachable endpoint
    - fails on status other than 202 Accepted
2. Retrieve the resource via GET `/jsonstore/{id}`
    - retries twice with timeout (in case the queue is slower than expected)
    - fails if the resource created in 1. does not have a valid `.href` in the body
    - fails if after N retries no 200 OK is received.


In order to run integration tests, first make sure that an app is either deployed
locally or running on a remote host with all of its dependencies available (in which case
use env variable INTEGRATION_TEST_BASE_URL_PORT to direct the tests against it). 

Execute using:
```
./gradlew integrationTest
```

---

### Environment variables:

| variable | default | description |
|---|---|---|
| `MONGODB_URL` | localhost | url of the mongo db instance to connect to|
| `MONGODB_PORT` | 27017 | port of the mongodb instace to connect to|
| `KAFKA_BOOTSTRAP_SERVERS` | localhost:9092 | comma-separated list of host and port pairs that are the addresses of Kafka brokers|
| `INTEGRATION_TEST_BASE_URL_PORT` | http://127.0.0.1:8080 | Location to run acceptance tests against |

---

### Potential improvements:

- Implement a dead letter queue for messages that were not stored
- Add common error handling to have consistent messages.
- Add common logging for each accepted request.
- Аdd embedded kafka/mongo to allow for integration tests w/o dependencies

---

### Technologies used:

- [Java 11](https://adoptopenjdk.net/releases.html?variant=openjdk11&jvmVariant=hotspot)
- [Spring Framework, Boot and various starters](https://spring.io/projects/spring-boot)
- [docker and docker-compose](https://docs.docker.com/)
- [apache kafka](https://kafka.apache.org/)
- [mongodb](https://www.mongodb.com/what-is-mongodb)