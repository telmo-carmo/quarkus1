#!/bin/bash

nohup java -jar target/quarkus1-1.0.0-SNAPSHOT-runner.jar > quarkus1.log  2>&1  &
echo $! > quarkus1.pid
# http://10.221.132.29:8080/swagger-ui/index.html

if [ -f quarkus1.pid ]; then
    PID=$(cat quarkus1.pid)
    echo "Quarkus1 server started with PID $PID"
fi
