#!/usr/bin/env bash

pushd `dirname $0`/..

docker-compose run --rm --service-ports web lein repl

trap popd EXIT
