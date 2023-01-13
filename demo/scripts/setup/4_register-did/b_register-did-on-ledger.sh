#!/usr/bin/env sh

# DIDs and their Verkey can be registered on https://selfserve.indiciotech.io/ alternatively

if [ $# -ne 2 ]; then
    echo "missing arguments"
    echo "$0 did verkey"
    exit 1
fi

DATA=$(printf '{"network":"testnet","did":"%s","verkey":"%s"}' "$1" "$2")

curl 'https://selfserve.indiciotech.io/nym' \
  -H 'Accept: */*' \
  -H 'Content-Type: application/x-www-form-urlencoded; charset=UTF-8' \
  --data-raw "$DATA" \
  --compressed
