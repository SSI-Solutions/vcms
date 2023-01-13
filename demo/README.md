# Demo

This page explains how to quick-start VCMS locally and use it to prototype.

## Requirement

- Install ngrok locally - https://ngrok.com/
- Create a ngrok account and add the authentication token to the cli
- Install a SSI wallet on your phone - search Lissi on the app store
- Install Docker and the docker compose plugin

## Step 0: Information

The following demo was tested on macOS and is compatible with most Linux distribution.
It may also work on Windows with `Windows Subsystem for Linux` or even terminal emulators like `Git Bash`.
Most script just contains curl requests and can also be translated to other tools.

## Step 1: Setup

The folder `demo/scripts/setup/` contains the setup steps for the VCMS.
Steps are to be executed in numeric order.

1. Start ngrok to tunnel the required service to a publicly available url (`1_tunnel-required-port`)
2. Continue in a new terminal tab / window
3. Change the configuration of VCMS to adapt the `SEED` and `DID_ENDPOINT_URL`variables (`2_configure-acapy`)
   1. `SEED` can be anything, as long as it is unique per VCMS instance and not too short
   2. `DID_ENDPOINT_URL` is the public forwarded URL given by ngrok
4. Start Docker and run `docker-compose up -d` from within `demo/docker/`. See the troubleshoot chapter on issues
   1. It takes a moment for all docker images to initialize. You can verify it with `docker stats`.
   When ready, docker images should only consume few percent of the CPU.
5. Go back to `demo/scripts/setup/`
6. Accept the Author Agreement to use the Indy Ledger (`3_accept-taa`)
7. Register a decentralized identifier (did) on the indy ledger (`4_register-did`)
   1. Get the local did and its verification key
   2. Register the did and verkey on the indy ledger
   3. Assign the public did to the VCMS
   4. Verify that the public did is correctly assigned
      1. When the command return the did from the first command, the setup was successful

## Step 2: Create, issue and verify a credential

The folder `demo/examples/` contains scripts that show how to use the VCMS.
Scripts can be executed in numeric order to test the normal use case of a credential.

1. Create a credential schema and a credential definition (only done once per credential) (`1_create-a-credential-schema`)
2. Create a connection between VCMS and a mobile wallet (`2_connection-to-wallet`)
   1. Verify that the connection was successful before continuing
3. Issue  a credential to a wallet from the credential definition created earlier (`3_issue-credential-to-wallet`)
4. Verify a credential from a mobile wallet and get the claims of the credential (`4_verify-wallet-credential`)
5. Verify a credential from another issuer (VCMS instance) (`5_verify-credentials-from-other-issuer`)

## Demo User Interface

There are two demo user interface available with the local deployment:

- Issuer UI - Issue credentials to a wallet (revocation does not work in this demo)
  - http://localhost:81
- Verifier UI - Verifies credentials from a wallet and shows its claims.
  - http://localhost:83

## API Documentation

Every service also has an API documentation endpoint where API are explained and can be tested:

- Connection API
  - http://localhost:8080/swagger-ui/index.html
- Issuer API
  - http://localhost:8100/swagger-ui/index.html
- Verifier API
  - http://localhost:8081/swagger-ui/index.html
- ACA-PY SSI APIs
  - http://localhost:9020/swagger-ui/index.html

## Troubleshoot

On some OSes, VCMS will not start because of restricted permissions on the `demo/docker/wallet` and `demo/docker/tails-file` folders.

