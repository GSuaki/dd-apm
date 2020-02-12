#/bin/bash

./gradlew clean bootJar

sudo docker build -t webflux -f NRDockerfile .
sudo docker-compose up -d --force-recreate