FROM ghcr.io/bcgov/tails-server:1.0.0


ADD util/start_tails_server.sh ./start_tails_server.sh
RUN cat ./start_tails_server.sh

ENTRYPOINT ["bash", "./start_tails_server.sh"]
