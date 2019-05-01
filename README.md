# Spring kafka mongodb - sample project

The following project implements a simple data pipeline, utilizing SpringBoot, Kafka and MongoDB.

Springboot's embedded Tomcat is used to serve REST endpoints.

# API usage

<!--TODO(vic)-->

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

# Dev setup

<!--TODO(vic)-->

---

### Technologies used:

- [Java 11](https://adoptopenjdk.net/releases.html?variant=openjdk11&jvmVariant=hotspot)
- [Spring Framework, Boot and various starters](https://spring.io/projects/spring-boot)
- [docker and docker-compose](https://docs.docker.com/)
- [apache kafka](https://kafka.apache.org/)
- [mongodb](https://www.mongodb.com/what-is-mongodb)