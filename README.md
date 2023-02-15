[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=vcms&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=vcms)
# Verifiable Credential Management System (VCMS)

VCMS is a system that abstract SSI technologies and enables technology-agnostic integration in existing systems.

The system currently offers the following features:

- Create and manage connections between VCMS and the SSI wallets
- Create, manage and revoke VCs
- Verify VCs from SSI wallets (VC issued by the VCMS and third-parties)

## Documentation

Documentation on the system can be found in the [Wiki](https://github.com/SSI-Solutions/vcms/wiki)

### Demo

The folder `demo` contains a step by step demo where VCMS can be tried out on a local machine.

## Project Structure
For module naming we use the following structure:

- `svc-*` --> Internal service module.
- `api-*` --> Provides a public facing API for integration.
- `ui-*`  --> Frontend component providing a web gui.

## Using the project

### Building the app from source

**Assemble the binaries**

`./gradlew assemble`

**Build and Test (requires a docker daemon running)**

`./gradlew build`

**Build Docker Images (requires a docker daemon running)**

`./gradlew buildAllImages`

### Development

We have the following environments. The environment variables should be documented in the `*.env` files directly.
- `DEV`: Used for local testing and development, every port is open on localhost

#### Notes for windows development :

- Install `ngrok` through `chocolatey`. Start `Command Prompt` as admin to use it.
- There might be issues with line-endings, use `Git Bash` to fix them: `find . -type f -name "*.sh"  -print0  | xargs -0  dos2unix`
- Rebuild the affected images.

### Contributing

see [CONTRIBUTING.md](https://github.com/SSI-Solutions/vcms/blob/main/CONTRIBUTING.md)
