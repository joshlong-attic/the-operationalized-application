#!/bin/bash

set -e

# see https://github.com/SonarSource/docker-sonarqube for more

docker run -d --name sonarqube -p 9000:9000 -p 9092:9092 sonarqube:5.1
mvn sonar:sonar -Dsonar.host.url=http://$(boot2docker ip):9000 -Dsonar.jdbc.url="jdbc:h2:tcp://$(boot2docker ip)/sonar"
