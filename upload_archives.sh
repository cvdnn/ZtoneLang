#!/bin/bash

_GIT_PATH='/workon/m2/mvn-repo'

./gradlew -q -p ztone.lang clean build uploadArchives

git -C ${_GIT_PATH} add ./
git -C ${_GIT_PATH} commit -m "ZTONE LANG"
git -C ${_GIT_PATH} push github master