# Verify credentials from other issuers

To be able to verify external credentials, the credential definition needs to be added to the configuration of the verifier component.

In file `demo/docker/configuration-verifier-server.env` make the following changes:

- Add `SSI_CONFIGURATION_CRE_DEF_IDS` and assign comma delimited credential definition ids


After rebooting the verifier component, the added credential definition can be visualized in the verifier demo UI.
  `
