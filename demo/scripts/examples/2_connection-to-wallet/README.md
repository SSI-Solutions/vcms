# Connection between VCMS and a wallet

There are two important information generated during the creation of a connection.

- `InvitationUrl`
  - The invitation url contains the pointer to the VCMS and the key to encrypt the messages send to the VCMS from a wallet.
  The most common use now, is to generate a QR code from it and scan it with a SSI wallet.
- `ConnectionId`
  - The connection id represent the connection between the VCMS and the SSI wallet.
  It is used for every action in VCMS to trigger an action with a wallet user.
