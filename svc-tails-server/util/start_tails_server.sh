#!/bin/bash

[ -z "$TAILS_SERVER_IP" ] && echo "Missing TAILS_SERVER_IP exiting" && exit 1
[ -z "$TAILS_SERVER_PORT" ] && echo "Missing TAILS_SERVER_PORT exiting" && exit 1
[ -z "$TAILS_SERVER_LOG_LEVEL" ] && echo "Missing TAILS_SERVER_LOG_LEVEL exiting" && exit 1
[ -z "$TAILS_SERVER_DATA_FOLDER" ] && echo "Missing TAILS_SERVER_DATA_FOLDER exiting" && exit 1


echo "Starting Tails File Server ..."
echo -----------------------
echo "Listening:   TAILS_SERVER_IP:  ${TAILS_SERVER_IP}"
echo "Listening:   TAILS_SERVER_PORT: ${TAILS_SERVER_PORT}"
echo "TAILS_SERVER_LOG_LEVEL      ${TAILS_SERVER_LOG_LEVEL}"
echo "TAILS_SERVER_DATA_FOLDER:      ${TAILS_SERVER_DATA_FOLDER}"
echo -----------------------

#set configuration for the tails-server start

#create directory for data if
mkdir -p ${TAILS_SERVER_DATA_FOLDER}

#docker rm -f tails-server

tails-server \
--host ${TAILS_SERVER_IP} \
--port ${TAILS_SERVER_PORT} \
--log-level ${TAILS_SERVER_LOG_LEVEL} \
--storage-path ${TAILS_SERVER_DATA_FOLDER} \
