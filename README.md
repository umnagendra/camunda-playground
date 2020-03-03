# camunda-playground
Learning Camunda BPM

## Overview
This is a simple spring boot app that illustrates the use of [Camunda BPM]() to execute workflows.

## Design
- Embedded Camunda BPM engine
- MySQL as the persistence layer (data access via spring JPA)
- REST API to trigger workflows, approve actions in workflows

### Workflows 
A simple BPMN workflow is created (using [Camunda Modeler]()) that has service tasks to obtain random numbers and check for even numbers.    
See [random_workflow.bpmn](src/main/resources/random_workflow.bpmn)

![image](https://user-images.githubusercontent.com/990210/75753291-77fc1f00-5d50-11ea-9a45-ddda731d10bc.png)

## Build
`mvn clean install`

## Run
This application requires a MySQL database for use by Camunda BPM    
Refer [Running MySQL as a Docker Container](#running-mysql-as-a-docker-container) for details.

```shell
export SERVER_PORT=<port number where the server will listen for incoming REST API requests>
export DB_HOST=<host where MySQL DB is running (could be localhost)>
export DB_USERNAME=<db username>
export DB_PASSWORD=<db password>
mvn spring-boot:run
```

### APIs
#### Start a workflow
Simply do a `POST` to `http://<host>:<port>/start-workflow` with the below JSON payload.

```javascript
{
	"workflowKey": "<Workflow Key>",
    "businessKey": "<Any unique custom key>",
	"startedBy": "<Your Name>"
}
```    

The actual process instance is executed asynchronously, and the client receives a `202 ACCEPTED` response.    
Follow logs on STDOUT for progress of the triggered workflow.

#### Approve a workflow _(waiting to get another random number)_
Simply do a `POST` to `http://<host>:<port>/approve-workflow` with the below JSON payload.

```javascript
{
	"workflowKey": "<Workflow key of process waiting>",
    "businessKey": "<Unique custom key of process waiting>",
	"approvedBy": "<Your Name>"
}
```

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

### Clustering
Clustering the camunda engine in this app is as simple as running multiple instances of this app, all of which should point to a single instance of MySQL.    
When multiple instances of this app are run as a cluster, ongoing tasks initiated in any app will be taken over and continued by another running instance in the cluster.