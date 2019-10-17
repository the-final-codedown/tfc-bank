#!/bin/bash

mvn clean install

cp ./target/tfc-profile-0.0.1-SNAPSHOT.jar ./src/docker/
docker-compose up --build -d