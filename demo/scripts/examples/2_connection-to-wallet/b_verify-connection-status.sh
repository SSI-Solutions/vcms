#!/usr/bin/env sh

# API Documentation -> http://localhost:8080/swagger-ui/index.html

if [ $# -ne 1 ]; then
    echo "missing argument"
    echo "$0 connectionId"
    exit 1
fi

URL=$(printf 'http://localhost:8080/connection/%s' "$1")

curl -X 'GET' \
  "$URL" \
  -H 'accept: application/json'
