#!/usr/bin/env bash

set -feu

readonly maxtime=20m

[[ $# -gt 0 ]] && exec &> "$1"

timeout -k 1m "$maxtime" sbt --batch --color=never -Djline.terminal=none clean 'test:runMain grading.Grade'
