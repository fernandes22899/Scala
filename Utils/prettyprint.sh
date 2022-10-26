#!/usr/bin/env sh

# v. 1.0
#
# Highlights source code using pandoc.
# Language is taken to be the file extension (e.g., java, scala, c),
# except that a '.sc' extension is taken to be Scala (worksheets)
# and a '.py' extension is taken to be Python.
#
# (c) Michel Charpentier

set -feu

for f in "$@"; do
  file="${f%.*}"
  ext="${f##*.}"
  [ "$ext" = sc ] && ext=scala
  [ "$ext" = py ] && ext=python
  printf "%s -> %s.pdf\n" "$f" "$file"
  (
    printf "# %s\n\n" "$f"
    printf "~~~ {.%s .numberLines}\n" "$ext"
    cat "$f"
    [ -z "$(tail -c 1 "$f")" ] || echo
    printf '~~~\n'
  ) | pandoc -V geometry='margin=2cm' --highlight-style=monochrome -o "$file.pdf"
done
