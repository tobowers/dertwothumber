#!/usr/bin/env bash

command_exists () {
    type "$1" &> /dev/null ;
}

if command_exists docker-machine; then
  exec ngrok http 192.168.99.100:3000
else
  exec ngrok http localhost:3000
fi
