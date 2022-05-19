@echo off
call mvn clean package
call docker build -t fr.acln/game-apis .
call docker rm -f game-apis
call docker run -d -p 9080:9080 -p 9443:9443 --name game-apis fr.acln/game-apis