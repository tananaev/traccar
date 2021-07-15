#!/bin/bash

which grep &> /dev/null  || { echo >&2 "grep package cant be found on path. Aborting."; exit 1; }
which docker &> /dev/null  || { echo >&2 "Docker package cant be found on path. Aborting."; exit 1; }

export company=${1:-"tananaev"}
export software=${2:-"traccar"}
export _version=$(head -n 10 ./pom.xml |grep version|cut -d ">" -f2|cut -d"<" -f1)
export version=${3:-$_version}

docker build -t ${company}/${software}:${version} -f ./setup/docker/Dockerfile .