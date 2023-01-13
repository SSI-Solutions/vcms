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

ENTRYPOINT ["/bin/sh", "-c" , "http-server -p 80 -d false"]
EXPOSE 80
