#!/usr/bin/env bash

echo "|-- 启动前端: ./start.sh f  --|"
echo "└-- 启动后端: ./start.sh b  --|"
for i in "$@"; do
  if [ $i == 'init-f' ]
  then
    cd ./client
    npm install
    npm run serve
  fi
  if [ $i == 'f' ]
  then
    cd ./client
    npm run serve
  fi
  if [ $i == 'b' ]
  then
    cd ./server
    gradle build -x test
    docker build -t marsback .
    cd ../dev-docker
    docker-compose up
  fi
  if [ $i == 'init-b' ]
  then
    cd ./server
    gradle build -x test
    docker build -t marsback .
    cd ../dev-docker
    rm -rf base/usr/mysql/data
    docker-compose up
  fi
  if [ $i == 'fb' ]
  then
    cd ./client
    npm run serve
    cd ../server
    gradle build -x test
    docker build -t marsback .
    cd ../dev-docker
    docker-compose up
  fi
done
# done
