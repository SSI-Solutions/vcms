# define a credential

Defining a credential that should be issued is done in two steps.

## 1. Create a credential schema

The credential schema define all attributes that the credential should have.

see `a_create-credential-schema.sh` for example

## 2. Create a credential definition

Before you continue, you need to get the schema id from the previous step to be able to define a credential.
The credential definition adds more metadata to the schema and makes it issuable.

see `b_define-credential-without-revocation.sh` for example.

### Notes
The local VCMS demo does currently not support revocation for credentials.
This is due to the port tunneling being limited to one port on the free ngrok license.
Revocation support can be added if the tails server is publicly available and its URL is configured in `../../demo/docker/configuration-aca-py.env`
