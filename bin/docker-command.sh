#!/usr/bin/env bash

set -e

pushd `dirname $0`/..

docker-compose run --rm --service-ports web lein ${@}

trap popd EXIT
