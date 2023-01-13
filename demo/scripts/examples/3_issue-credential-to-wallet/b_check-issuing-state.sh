#!/usr/bin/env sh

# API Documentation -> http://localhost:8100/swagger-ui/index.html

if [ $# -ne 1 ]; then
    echo "missing argument"
    echo "$0 processId"
    exit 1
fi

URL=$(printf 'http://localhost:8100/issue/process/%s/state' "$1")

curl -X 'GET' \
  "$URL" \
  -H 'accept: application/json'
