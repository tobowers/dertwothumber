#!/usr/bin/env bash

set -e

pushd `dirname $0`/..

bin/docker-command.sh test

trap popd EXIT
