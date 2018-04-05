# hello-dataloader

Repo to demonstrate some performance issues with graphl-java

## Build and package

    ./gradlew assemble

This creates `build/distributions/hello-dataloader-1.0-SNAPSHOT.tar`

## Start a server
    
    tar xf build/distributions/hello-dataloader-1.0-SNAPSHOT.tar
    cd hello-dataloader-1.0-SNAPSHOT/bin/
    ./hello-dataloader

## Run load tests

    cd src/test/resources
    ./loadtest.sh
