#!/bin/bash

[ -z "$LEDGER_URL" ] && echo "Missing LEDGER_URL exiting" && exit 1
[ -z "$ADMIN_IP" ] && echo "Missing ADMIN_IP exiting" && exit 1
[ -z "$ADMIN_PORT" ] && echo "Missing ADMIN_PORT exiting" && exit 1
[ -z "$ENDPOINT_IP" ] && echo "Missing ENDPOINT_IP exiting" && exit 1
[ -z "$ENDPOINT_PORT" ] && echo "Missing ENDPOINT_PORT exiting" && exit 1
[ -z "$DID_ENDPOINT_URL" ] && echo "Missing DID_ENDPOINT_URL exiting" && exit 1
[ -z "$WEBHOOK_IP" ] && echo "Missing WEBHOOK_IP exiting" && exit 1
[ -z "$WEBHOOK_PORT" ] && echo "Missing WEBHOOK_PORT exiting" && exit 1
[ -z "$LABEL" ] && echo "Missing LABEL exiting" && exit 1
[ -z "$WALLET_NAME" ] && echo "Missing WALLET_NAME exiting" && exit 1
[ -z "$WALLET_KEY" ] && echo "Missing WALLET_KEY exiting" && exit 1


ADMIN_URL="http://${ADMIN_IP}:${ADMIN_PORT}"
ENDPOINT_URL="http://${ENDPOINT_IP}:${ENDPOINT_PORT}"
WEBHOOK_URL="http://${WEBHOOK_IP}:${WEBHOOK_PORT}"

echo "Starting wallet ..."
echo -----------------------
#host $HOSTNAME
echo "Listening:   ADMIN_URL:        ${ADMIN_URL}"
echo "Listening:   ENDPOINT_URL:     ${ENDPOINT_URL}"
echo "Connecting:  WEBHOOK_URL:      ${WEBHOOK_URL}"
echo "Connecting:  LEDGER_URL:       ${LEDGER_URL}"
echo "Agent addr:  DID_ENDPOINT_URL: ${DID_ENDPOINT_URL} (must be accessible by other agents )"
echo "Walletname:  WALLET_NAME:      ${WALLET_NAME}"
echo "Label:        ${LABEL}"
echo -----------------------

aca-py start \
   --auto-provision \
  --debug-connections \
  --debug-credentials \
  --debug-presentations \
  --auto-store-credential \
  --auto-verify-presentation \
  --monitor-ping \
  --admin "${ADMIN_IP}" "${ADMIN_PORT}" \
  --endpoint "${DID_ENDPOINT_URL}" \
  --inbound-transport http "${ENDPOINT_IP}" "${ENDPOINT_PORT}" \
  --outbound-transport http \
  --admin-insecure-mode \
  --webhook-url "http://${WEBHOOK_IP}:${WEBHOOK_PORT}" \
  --genesis-url "${LEDGER_URL}" \
  --label "${LABEL}" \
  --debug-connections \
  --public-invites \
  --wallet-type indy \
  --wallet-name "${WALLET_NAME}" \
  --wallet-key "${WALLET_KEY}"
