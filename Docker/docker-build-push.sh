#!/usr/bin/env bash

./gradlew clean build
docker build -f Docker/Dockerfile --no-cache -t J3T4R0/storefront-cases:latest .
docker push J3T4R0/storefront-cases:latest

# docker run --name storefront-cases -d J3T4R0/storefront-cases:latest