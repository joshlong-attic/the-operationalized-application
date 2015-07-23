#!/bin/bash

set -e

seq 1000 | while read l ; do
  curl -H "Content-Type: application/json" -d  "{\"sku\":\"sku1${l}\"}"   http://localhost:8080/products   ;
  s=`gshuf -i 1-10 -n 1`
  echo
  echo 0.$s
  sleep 0.$s
done
