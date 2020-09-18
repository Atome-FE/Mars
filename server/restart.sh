#!/usr/bin/env bash
# docker-compose rm tboss-app
# docker-compose run tboss-app -d
# docker-compose up tboss-app
docker build -t tboss-app .
docker stack rm tboss-app
docker stack deploy -c docker-compose.yml tboss-app
