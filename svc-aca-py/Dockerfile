FROM ghcr.io/hyperledger/aries-cloudagent-python:py3.6-indy-1.16.0-0.8.1

ADD util/start_wallet_from_seed.sh ./start_wallet_from_seed.sh
RUN cat ./start_wallet_from_seed.sh

ENTRYPOINT ["bash", "./start_wallet_from_seed.sh"]
