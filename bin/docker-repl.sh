#!/usr/bin/env bash

set -e

pushd `dirname $0`/..

bin/docker-command.sh repl

trap popd EXIT
