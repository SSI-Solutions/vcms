#!/usr/bin/env sh

if [ $# -ne 1 ]; then
    echo "missing argument"
    echo "$0 did"
    exit 1
fi

URL=$(printf 'http://localhost:9020/wallet/did/public?did=%s&create_transaction_for_endorser=false' "$1")

curl -X 'POST' \
  "$URL" \
  -H 'accept: application/json' \
  -d ''
