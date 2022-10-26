# How to pretty-print code

## Using `prettyprint`

Copy the `prettyprint` script somewhere in the executable path.  Then:

~~~bash
prettyprint File.scala
~~~

Note that `prettyprint` uses `pandoc`, which should be installed on `mica` (see <https://pandoc.org> to install on a personal computer).

## Using `a2ps`

~~~bash
a2ps -1 -Ppdf File.scala
~~~

`a2ps` should be installed on `agate` and `mica` (see <https://www.gnu.org/software/a2ps/> to install on a personal computer).

## Using IntelliJ IDEA

The menu `File` $\to$ `Print` can be used to generate a pdf via a virtual printer.