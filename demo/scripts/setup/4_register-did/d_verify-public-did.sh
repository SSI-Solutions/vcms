#!/usr/bin/env sh

curl -X 'GET' \
  'http://localhost:9020/wallet/did/public' \
  -H 'accept: application/json'
