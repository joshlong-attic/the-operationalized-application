#!/bin/bash 

set -e 

docker run -d --name opentsdb -p 60000:60000 -p 60010:60010 -p 60030:60030 -p 4242:4242 petergrace/opentsdb-docker:latest

docker run -d --name metrilyx-dashboard  -e ACCESS_ADDRESS="`boot2docker ip`:8081" -p 8081:80   --link opentsdb:OPENTSDB dreampuf/metrilyx:latest


# docker run -d --name metrilyx-dashboard -p 80:80 --link opentsdb:OPENTSDB dreampuf/metrilyx:latest