#!/usr/bin/env bash

_PROJECT_PATH='/workon/m2/mvn-repo'

git status
git add ./
git commit -m "ZTONE_LANG"
git push github master

if (( $# == 0 )) || [[ -z $1 ]]; then
    ./gradlew -q -p ztone.lang clean build uploadArchives

else
    _MODULE_NAME=$1

    ./gradlew -q -p ${_MODULE_NAME} clean build uploadArchives
fi

git -C ${_PROJECT_PATH} status
git -C ${_PROJECT_PATH} add ./
git -C ${_PROJECT_PATH} commit -m "ZTONE_LANG"
git -C ${_PROJECT_PATH} push github master
