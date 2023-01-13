# Configuring VCMS

Some configurations must be adapted before starting the VCMS.

in the file `../../../demo/docker/configuration-aca-py.env`, following changes have to be made:

- **SEED**: value used to generate random values (for cryptography for example).
If you use the same value as others, you will get the same dids.
- **DID_ENDPOINT_URL**: publicly available URL to communicate between wallet and server.
Use the URL given by ngrok.
