#!/usr/bin/env sh

if [ $# -ne 2 ]; then
    echo "missing arguments"
    echo "$0 schema_id tag"
    exit 1
fi

DATA=$(printf '{"revocation_registry_size": 1000, "schema_id": "%s", "support_revocation": false,"tag": "%s"}' "$1" "$2")

curl -X 'POST' \
  'http://localhost:9020/credential-definitions?create_transaction_for_endorser=false' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d "$DATA"
