# camunda-playground
Process workflows using Camunda BPM Engine

- [Overview](#overview)
- [Design](#design)
    - [Workflows](#workflows)
        - [1. Random Number Generation Workflow](#1-random-number-generation-workflow)
        - [2. Scheduled Stock Quotes Email Workflow](#2-scheduled-stock-quotes-email-workflow)
- [Build](#build)
- [Run](#run)
    - [APIs](#apis)
        - [Start a workflow](#start-a-workflow)
        - [Approve a workflow _(waiting to get another random number)_](#approve-a-workflow-_waiting-to-get-another-random-number_)
        - [List (GET) all workflows](#list-get-all-workflows)
        - [Delete a workflow](#delete-a-workflow)
    - [Running MySQL as a Docker Container](#running-mysql-as-a-docker-container)
    - [Clustering](#clustering)

## Overview
This is a simple spring boot app that illustrates the use of [Camunda BPM]() to execute workflows.

## Design
- Embedded Camunda BPM engine
- MySQL as the persistence layer (data access via spring JPA)
- REST API to trigger workflows, approve actions in workflows
- SMTP integration (with configured external SMTP service) to send emails

### Workflows
The below workflows are created (using [Camunda Modeler]()).

#### 1. Random Number Generation Workflow
Has service tasks to obtain random numbers and check for even numbers.    
If the generated random number is not even, the workflow moves to a wait-state until it receives a signal (approval) to get another random number.
    
See [random_workflow.bpmn](src/main/resources/random_workflow.bpmn)

![image](https://user-images.githubusercontent.com/990210/75753291-77fc1f00-5d50-11ea-9a45-ddda731d10bc.png)

- - -

#### 2. Scheduled Stock Quotes Email Workflow
Has a periodic schedule that triggers a service task to get stock quotes for a set of symbols (using APIs from [alphavantage.co](https://www.alphavantage.co/)).
The received stock quotes are transformed (in a subsequent task) into a HTML report and sent via email.

See [stock_price_email_workflow.bpmn](src/main/resources/stock_price_email_workflow.bpmn)

![image](https://user-images.githubusercontent.com/990210/76106651-4d82be00-5ffd-11ea-80ff-fe9e83d308d6.png)

## Build
`mvn clean install`

## Run
This application requires a MySQL database for use by Camunda BPM    
Refer [Running MySQL as a Docker Container](#running-mysql-as-a-docker-container) for details.

```shell
export SERVER_PORT=<port number where the server will listen for incoming REST API requests>

# For camunda persistence and clustering
export DB_HOST=<host where MySQL DB is running (could be localhost)>
export DB_USERNAME=<db username>
export DB_PASSWORD=<db password>

# For email sending (Scheduled Stock Quotes Email Workflow)
export EMAIL_SMTP_HOST=<Fully-qualified hostname of SMTP server>
export EMAIL_USERNAME=<username in SMTP server>
export EMAIL_PASSWORD=<password in SMTP server>
export EMAIL_TO_ADDRESSES=<comma-separated list of valid email addresses to which the stock report will be sent>
export EMAIL_REPLY_TO_ADDRESS=<Reply-To address for sent emails>

# Run the damn app already!
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

#### List (GET) all workflows
Simply do a `GET` to `http://<host>:<port>/workflow` and expect a JSON response.

```javascript
[
    {
        workflowKey: "RandomWorkflow",
        businessKey: "Tenant1583236658",
        active: true
    },
    {
        workflowKey: "RandomWorkflow",
        businessKey: "Tenant1583236676",
        active: true
    },
    {
        workflowKey: "RandomWorkflow",
        businessKey: "Tenant1583236677",
        active: true
    },
    . . .
]
```

#### Delete a workflow
Simply do a `DELETE` to `http://<host>:<port>/workflow/{workflowKey}/{businessKey}` to delete a specific active workflow.
 
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