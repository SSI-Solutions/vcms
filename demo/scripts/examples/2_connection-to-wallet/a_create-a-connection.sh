#!/usr/bin/env sh

# API Documentation -> http://localhost:8080/swagger-ui/index.html

curl -X 'POST' \
  'http://localhost:8080/connection/invitation' \
  -H 'accept: application/json' \
  -d ''
