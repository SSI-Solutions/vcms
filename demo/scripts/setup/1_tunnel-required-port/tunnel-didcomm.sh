#!/usr/bin/env sh

if ! command -v "ngrok" > /dev/null; then
  echo "ngrok is required to run this script."
fi

while :
do
    ngrok http --scheme http 8044
done
