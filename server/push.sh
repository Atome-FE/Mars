#!/usr/bin/env bash
gradle build -x test
scp build/libs/suda-0.0.1-SNAPSHOT.jar stg-finance-biz2:/data/docker-apps/t-boss/back/
