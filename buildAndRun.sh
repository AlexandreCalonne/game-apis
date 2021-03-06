#!/bin/sh
mvn clean package && docker build -t fr.acln/game-apis .
docker rm -f game-apis || true && docker run -d -p 9080:9080 -p 9443:9443 --name game-apis fr.acln/game-apis