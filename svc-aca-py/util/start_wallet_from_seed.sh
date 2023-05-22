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
[ -z "$TAILS_SERVER_URL" ] && echo "Missing TAILS_SERVER_URL exiting" && exit 1

ADMIN_URL="http://${ADMIN_IP}:${ADMIN_PORT}"
ENDPOINT_URL="http://${ENDPOINT_IP}:${ENDPOINT_PORT}"
WEBHOOK_URL="http://${WEBHOOK_IP}:${WEBHOOK_PORT}"
WALLET_PATH="/home/indy/.indy_client/wallet/${WALLET_NAME}"

if [[ ${ACAPY_ENDORSER_ROLE} = 'author' ]]
then
	[ -z "$ACAPY_CREATE_REVOCATION_TRANSACTIONS" ] && echo "Missing ACAPY_CREATE_REVOCATION_TRANSACTIONS exiting" && exit 1
	[ -z "$ACAPY_AUTO_WRITE_TRANSACTIONS" ] && echo "Missing ACAPY_AUTO_WRITE_TRANSACTIONS exiting" && exit 1
	[ -z "$ACAPY_AUTO_REQUEST_ENDORSEMENT" ] && echo "Missing ACAPY_AUTO_REQUEST_ENDORSEMENT exiting" && exit 1
	[ -z "$ACAPY_ENDORSER_ALIAS" ] && echo "Missing ACAPY_ENDORSER_ALIAS exiting" && exit 1
	[ -z "$ACAPY_ENDORSER_PUBLIC_DID" ] && echo "Missing ACAPY_ENDORSER_PUBLIC_DID exiting" && exit 1
elif [[ ${ACAPY_ENDORSER_ROLE} = 'endorser' ]]
then
	[ -z "$ACAPY_AUTO_ENDORSE_TRANSACTIONS" ] && echo "Missing ACAPY_AUTO_ENDORSE_TRANSACTIONS exiting" && exit 1
else
	ACAPY_ENDORSER_ROLE='none'
fi

echo "Checking if wallet already exists at ${WALLET_PATH}."

if [[ ! -d "$WALLET_PATH" ]]
then
	[ -z "$SEED" ] && echo "Missing SEED exiting" && exit 1

	echo "Creating new wallet by provisioning ..."
    echo -----------------------
    echo "SEED: ${SEED}"
    echo "Please store this seed to recover your public DID."
    echo -----------------------

    aca-py provision \
      --seed "${SEED}" \
      --endpoint "${DID_ENDPOINT_URL}" \
      --wallet-type indy \
      --no-ledger \
      --wallet-name "${WALLET_NAME}" \
      --wallet-key "${WALLET_KEY}"
else
	echo "Wallet found."
fi

if [ -z "${ACAPY_DEBUG_WEBHOOKS}" ]; then
  echo "Defaulting webhooks to the verbose debug mode."
  export ACAPY_DEBUG_WEBHOOKS=true
fi


echo "Starting AcaPy agent ..."
echo -----------------------
echo "Listening:	  ADMIN_URL:			  ${ADMIN_URL}"
echo "Listening:	  ENDPOINT_URL:		  ${ENDPOINT_URL}"
echo "Connecting:	  WEBHOOK_URL:		  ${WEBHOOK_URL}"
echo "Connecting:	  LEDGER_URL:			  ${LEDGER_URL}"
echo "DID service:	DID_ENDPOINT_URL:	${DID_ENDPOINT_URL} (must be accessible by other agents)"
echo "Wallet name:	WALLET_NAME:		  ${WALLET_NAME}"
echo "Label:		    LABEL:				    ${LABEL}"
echo "Webhook debug mode:        ${ACAPY_DEBUG_WEBHOOKS}"
echo "TAILS_SERVER_URL:       		    ${TAILS_SERVER_URL}"
echo -----------------------

aca-py start \
--auto-verify-presentation \
--admin "${ADMIN_IP}" "${ADMIN_PORT}" \
--endpoint "${DID_ENDPOINT_URL}" \
--inbound-transport http "${ENDPOINT_IP}" "${ENDPOINT_PORT}" \
--outbound-transport http \
--admin-insecure-mode \
--webhook-url "${WEBHOOK_URL}" \
--genesis-url "${LEDGER_URL}" \
--label "${LABEL}" \
--wallet-type indy \
--wallet-name "${WALLET_NAME}" \
--wallet-key "${WALLET_KEY}" \
--tails-server-base-url "${TAILS_SERVER_URL}"
