import java.nio.file.Files

// Methods can be called with/without dots,
// and with/without parentheses in some cases
"foo".length
2 + 2
2+2
2.+(2)

3 < 2
3.<(2)

3 max 5

"Durham" -> 25143
"Durham".->(25143)

"foo" contains "o"
"foo".contains("o")

// (Notice the example of interoperability with Java)
val file = Files.createTempFile("foo", ".txt")
val out = Files.newBufferedWriter(file)

//Ideally, should use parentheses when method has side-effects
out.write("hello")
out write "hello" //works, but misleading!
out.newLine()
out.close()

println(2+2)
println({
  val two = 2
  two + two
})
println {
  val two = 2
  two + two
}

