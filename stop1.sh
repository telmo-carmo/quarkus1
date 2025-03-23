#!/bin/bash

if [ -f quarkus1.pid ]; then
    PID=$(cat quarkus1.pid)
    if kill -0 "$PID" 2>/dev/null; then
        kill "$PID"
        echo "Process $PID has been terminated."
    else
        echo "No process found with PID $PID."
    fi
    rm -f quarkus1.pid
else
    echo "PID file quarkus1.pid does not exist."
fi

#curl -X POST http://localhost:8080/actuator/shutdown

