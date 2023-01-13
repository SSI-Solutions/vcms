#!/usr/bin/env sh

curl -X 'POST' \
  'http://localhost:9020/schemas?create_transaction_for_endorser=false' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "attributes": [
    "score"
  ],
  "schema_name": "my_schema",
  "schema_version": "1.0"
}'
