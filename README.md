# camunda-playground
Learning Camunda BPM

## Overview

## Build

## Run
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