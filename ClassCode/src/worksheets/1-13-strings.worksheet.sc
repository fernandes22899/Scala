// You've seen basic strings with double quotes already.
// They're just Java strings with more methods available.
// Here are "raw"" strings (IntelliJ is best place to view these)
val text1 =
  """foo
     bar

     end"""

"[" + text1 + "]"

// Here's an example using stripMargin (can help
// make the strings easier to read in code)
val text2 =
  """foo
    |bar
    |
    |end""".stripMargin

"[" + text2 + "]"

// You can evaluate expressions inside strings,
// also known as string interpolation
val nums = List(1, 2, 3)

s"nums = $nums"
s"length of nums: ${nums.length}"

// While any object has toString,
// Collections all have mkString, allowing you to
// quickly construct strings from their contents
nums.mkString(" ")
nums.mkString("[", ",", "]")
nums.mkString

s"nums = ${nums.mkString("[", ",", "]")}"

s"22/7 = ${22.0 / 7.0}"
// There is also number formatting available
f"22/7 = ${22.0 / 7.0}%.3f"
