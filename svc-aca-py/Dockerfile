FROM bcgovimages/aries-cloudagent:py36-1.16-1_0.7.5

ADD util/start_wallet_from_seed.sh ./start_wallet_from_seed.sh
RUN cat ./start_wallet_from_seed.sh

ENTRYPOINT ["bash", "./start_wallet_from_seed.sh"]
