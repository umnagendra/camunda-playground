# camunda-playground
Learning Camunda BPM

## Overview
This is a simple spring boot app that illustrates the use of [Camunda BPM]() to execute workflows.

## Design
- Embedded Camunda BPM engine
- MySQL as the persistence layer (data access via spring JPA)
- REST API to trigger workflows

### Workflows 
A simple BPMN workflow is created (using [Camunda Modeler]()) that has service tasks to obtain random numbers and check for even numbers.
See [random_workflow.bpmn](src/main/resources/random_workflow.bpmn)

![image](https://user-images.githubusercontent.com/990210/75338406-abecc580-58b4-11ea-9ab3-405537a4ed99.png)

## Build
`mvn clean install`

## Run
This application requires a MySQL database for use by Camunda BPM
Refer [Running MySQL as a Docker Container](#running-mysql-as-a-docker-container) for details.

```shell
export DB_USERNAME=<db username>
export DB_PASSWORD=<db password>
mvn spring-boot:run
```

### Invoking APIs to start a workflow
Simply do a `GET http://<host>:<port>/start-workflow/<WorkflowKey>`    
The actual process instance is executed asynchronously, and the client receives a `202 ACCEPTED` response.    
Follow logs on STDOUT for progress of the triggered workflow.

### Running MySQL as a Docker Container
```
docker rm -f mysql && \
docker run \
    -p 3306:3306 \
    --name mysql \
    -e MYSQL_ROOT_PASSWORD=S0m3tH1n6
    -d mysql:latest
```

To connect to interactive SQL shell, use
```
docker exec -it mysql /bin/bash -c "mysql -u root -p"
<ENTER PASSWORD WHEN PROMPTED>
```

To create a database, (say `camunda_playground` for use by camunda BPM) use
```sql
create database camunda_playground;
use camunda_playground;
```