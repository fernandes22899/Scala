#!/usr/bin/env bash

# v. 0.5
#
# Compiles tests for the specified assignment
#
# (c) Michel Charpentier

# Processes only tags made of:
# ( a single digit, optionally followed by a single lowercase letter, OR
#   the word 'project' OR the word 'Project'
# ) optionally followed by a single period and one or more digits.
# Compilation takes place in the 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 or Project directory.
# Other tags are silently ignored.
#
# Examples tags: '1', '2a', '3b', '4.1', '5a.2', 'project', 'Project.3'
# Corresp. dir:   1    2     3     4      5       Project    Project

[[ ${BASH_VERSINFO:-0} -lt 4 ]] && echo >&2 "Bash version 4 or higher required." && exit 1
set -feu

[[ $# -lt 3 ]] && printf >&2 "Usage: prog <tag> <url> <branch>\n" && exit 1

tag=$1
instructorURL=$2
instructorBranch=$3

if [[ $tag =~ ^((([0-9])[a-z]?)|(Project|project))(\.[0-9]+)?$ ]]; then
  declare dir=${BASH_REMATCH[3]}${BASH_REMATCH[4]}
  dir=${dir^?} # capitalize 'project'
  cd "$dir"

  rm -rf src/test

  git remote remove testcode 2> /dev/null || true
  git remote add -f -t "$instructorBranch" testcode "$instructorURL"
  git restore --source=testcode/"$instructorBranch" -- src/test

  sbt --batch -Dsbt.ci=true test:compile
fi
