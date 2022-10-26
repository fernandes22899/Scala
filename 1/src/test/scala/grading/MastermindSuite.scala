package grading

import edu.unh.cs.mc.grading.GradingRun
import org.scalatest.time.SpanSugar._

class MastermindSuite extends SampleMastermindTests with GradingRun {

  override val longTimeLimit = 2.minutes

  import cs671.mastermind.Mastermind
  import org.scalactic.TimesOnInt._

  test("Too few or too many colors are invalid (2)") {
    assertThrows[IllegalArgumentException] {
      new Mastermind("R", Some("RRRR"))
    }
    assertThrows[IllegalArgumentException] {
      new Mastermind("ROYGBIVMCKW", Some("GBIV"))
    }
  }

  test("Too few or too many guess positions are invalid (2)") {
    assertThrows[IllegalArgumentException] {
      new Mastermind("RGBWYK", Some("G"))
    }
    assertThrows[IllegalArgumentException] {
      new Mastermind("RGBWYK", Some("RRGGBBWW"))
    }
  }

  test("No second argument generates code consistent with length 4, 2-color (1)") {
    val game = new Mastermind("BW", None)
    assert(game.numCodesRemaining == 16)
  }

  test("No second argument generates code consistent with length 4, 6-color (2)") {
    val game = new Mastermind("RGBYWK", None)
    assert(game.numCodesRemaining == 1296)
  }

  test("No second argument generates code consistent with length 4, 9-color (1)") {
    val game = new Mastermind("ROYGBIVWK", None)
    assert(game.numCodesRemaining == 6561)
  }

  test("generateAllPossibilities works even after guess is made (1)") {
    val game = new Mastermind("RGBYWK", Some("RGBW"))
    game.makeGuess("YKYK")
    val result: List[Any] = game.generateAllPossibilities
    assert(result.length == 1296)
    result.head match {
      case _: String => {
        assert(result contains "YYYY")
        assert(result contains "RGBW")
      }
      case _: List[Char] => {
        assert(result contains "YYYY".toList)
        assert(result contains "RGBW".toList)
      }
      case _ => fail("generateAllPossibilities returns wrong type")
    }
  }

  test("generateAllPossibilities doesn't interfere with other calls after guess made (2)") {
    val game = new Mastermind("RGBYWK", Some("RGBW"))
    game.makeGuess("YKYK")
    game.generateAllPossibilities
    assert(game.numCodesRemaining == 256)
    game.makeGuess("RGBW")
    game.generateAllPossibilities
    assert(game.solved == true)
  }

  test("evalutateGuess properly produces OOX result (2)") {
    val game = new Mastermind("RGBYWK", Some("RGBW"))
    assert(game.evaluateGuess("RGWK") == "OOX")
  }

  test("evalutateGuess properly produces XX result (2)") {
    val game = new Mastermind("RGBYWK", Some("RGBW"))
    assert(game.evaluateGuess("WKRY") == "XX")
  }

  test("evalutateGuess properly produces OX result with distractors (2)") {
    val game = new Mastermind("RGBYWK", Some("RWRR"))
    assert(game.evaluateGuess("RRGG") == "OX")
  }

  test("evalutateGuess properly produces X result with distractors (2)") {
    val game = new Mastermind("RGBYWK", Some("RWRR"))
    assert(game.evaluateGuess("KRKY") == "X")
  }

  test("evalutateGuess properly produces OXXXX result with 7 pegs (2)") {
    val game = new Mastermind("RGBYWK", Some("RRRGBBW"))
    assert(game.evaluateGuess("RGBWRYY") == "OXXXX")
  }

  test("Validate properly rejects inconsistent guess (2)") {
    val game = new Mastermind("RGBYWK", Some("RGBW"))
    game.makeGuess("YKYK")
    game.makeGuess("RRGG")
    assert(game.validate("BBWW") == false)
  }

  test("makeGuess on guess already made does not change remaining codes (2)") {
    val game = new Mastermind("RGBYWK", Some("RGBW"))
    game.makeGuess("YKYK")
    val size1 = game.numCodesRemaining
    game.makeGuess("YKYK")
    val size2 = game.numCodesRemaining
    assert(size1 == size2)
  }

  def checkDoubles(val1: Double, val2:Double) = {
    assert(f"$val1%.4f" == f"$val2%.4f")
  } 

  test("Guess of size 4 with 6 colors and some overlap evaluates properly (2)") {
    val game = new Mastermind("RGBYWK", Some("RGBW"))
    checkDoubles(game.analyzeGuess("RRGG"), 100*1040.0/1296)
  }

  test("Guess of size 6 with 5 colors and some overlap evaluates properly (2)") {
    val game = new Mastermind("RGBYW", Some("RRGBWW"))
    checkDoubles(game.analyzeGuess("RRGGYY"), 84.5632)
  }

  test("Guess of size 7 with 2 colors and some overlap evaluates properly (2)") {
    val game = new Mastermind("BW", Some("BWBBWWB"))
    checkDoubles(game.analyzeGuess("BBBWWWW"), 76.5625)
  }

  test("Guess of size 3 with 10 colors and some overlap evaluates properly (2)") {
    val game = new Mastermind("ROYGBIVMKW", Some("MOM"))
    checkDoubles(game.analyzeGuess("ROY"), 65.7)
  }

  test("Guess of size 6 with 5 colors after guess evaluates properly (2)") {
    val game = new Mastermind("RGBYW", Some("RRGBWW"))
    game.makeGuess("RRGGYY")
    checkDoubles(game.analyzeGuess("YWWGBR"), 81.701)
  }

  test("Guess of size 7 with 2 colors after guess evaluates properly (2)") {
    val game = new Mastermind("BW", Some("BWBBWWB"))
    game.makeGuess("BBBWWWW")
    checkDoubles(game.analyzeGuess("WBWBWBW"), 50)
  }

  test("Guess of size 3 with 10 colors after guess evaluates properly (2)") {
    val game = new Mastermind("ROYGBIVMKW", Some("MOM"))
    game.makeGuess("ROY")
    checkDoubles(game.analyzeGuess("MOI"), 62.5)
  }

  def playSolitaire(game: Mastermind) = {
    var codesLast = game.numCodesRemaining
    var codesNow = codesLast

    do {
      codesLast = codesNow
      val guess = game.generateBestGuess
      game.makeGuess(guess)
      codesNow = game.numCodesRemaining
      println(s"Made guess $guess, have $codesNow guesses remaining")
    } while (!game.solved && (codesNow < codesLast))
  }

  test("generateBestGuess advances 2-color, 6-peg game until solved (4)") {
    val game = new Mastermind("BW", Some("BBWWBW"))
    assert(!game.solved)
    playSolitaire(game)
    assert(game.solved)
  }

  import org.scalatest.tagobjects.Slow
  test("generateBestGuess advances default game until solved (4)", Slow) {
    val game = new Mastermind("RGBYWK", Some("RGBW"))
    assert(!game.solved)
    playSolitaire(game)
    assert(game.solved)
  }

  test("generateBestGuess advances 7-color 6-peg game until solved (3)", Slow) {
    val game = new Mastermind("ROYGBIV", Some("YORGBY"))
    game.makeGuess("VVVIII")
    println("Made guess VVVIII prior to asking for best guess")
    game.makeGuess("RRGGBB")
    println("Made guess RRGGBB prior to asking for best guess")
    assert(!game.solved)
    playSolitaire(game)
    assert(game.solved)
  }

  /*
  test("Cannot call makeguess after solved (1)") {
    //for another time when spec is written to cover this
  }
  */
}
