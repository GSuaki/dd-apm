#/bin/bash

./gradlew clean bootJar

sudo docker build -t webflux -f DDDockerfile .
sudo docker-compose up -d --force-recreate