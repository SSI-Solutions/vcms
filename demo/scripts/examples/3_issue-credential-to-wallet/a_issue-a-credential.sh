#!/usr/bin/env sh

# API Documentation -> http://localhost:8100/swagger-ui/index.html

if [ $# -ne 2 ]; then
    echo "missing arguments"
    echo "$0 connectionId credentialDefId"
    exit 1
fi

DATA=$(printf '{"connectionId":"%s","credentialDefinitionId":"%s","attributes": {"score": "125"}}' "$1" "$2")


curl -X 'POST' \
  'http://localhost:8100/issue/process' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d "$DATA"
