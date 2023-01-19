FROM node:16-slim AS build
WORKDIR /src/app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

FROM node:16-slim AS serve
RUN npm install http-server -g
COPY --from=build /src/app/dist/ui-verifier /dist
WORKDIR /dist

RUN groupadd -r http-user && useradd --no-log-init -r -g http-user http-user
RUN chown -R http-user /dist

USER http-user

ENV SERVER_PORT=""

ENTRYPOINT ["/bin/bash", "-c" , "http-server -p ${SERVER_PORT:-8200} -d false"]
