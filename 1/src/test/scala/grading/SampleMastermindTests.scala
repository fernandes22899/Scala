package grading

import edu.unh.cs.mc.grading.Controls
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.tagobjects.Slow

class SampleMastermindTests extends AnyFunSuite with Controls {

  import cs671.mastermind.Mastermind

  test("Six-color game with 4-color code has 1296 combitations (1)") {
    val game = new Mastermind("RGBYWK", Some("RGBW"))
    assert(game.numCodesRemaining == 1296)
  }

  test("Evaluation of Code returns OOOO (1)") {
    val game = new Mastermind("RGBYWK", Some("RGBW"))
    assert(game.evaluateGuess("RGBW") == "OOOO")
  }

  test("Evaluation of jumbled Code returns XXXX (1)") {
    val game = new Mastermind("RGBYWK", Some("RGBW"))
    assert(game.evaluateGuess("WRGB") == "XXXX")
  }

  test("Evaluation different from Code returns empty string (1)") {
    val game = new Mastermind("RGBYWK", Some("RGBW"))
    assert(game.evaluateGuess("YKYK") == "")
  }

  test("Randomly generated guesses are different and valid (1)") {
    val game = new Mastermind("RGBYWK", Some("RGBW"))
    var guesses = List[String]()
    for (i <- 1 to 10) {
      var guess = ""
      do {
        guess = game.generateGuess
      } while (guesses.contains(guess))
      //println("Testing " + guess)
      assert(game.validate(guess))
      guesses = guess :: guesses
    }
  }

  test("Guess with no overlap analyzes to 1040/1296 as percent (1)") {
    val game = new Mastermind("RGBYWK", Some("RGBW"))
    assert(f"${game.analyzeGuess("YKYK")}%.4f" == f"${100*1040.0/1296}%.4f")
  }

  test("Multiple guesses further reduce number of total guesses properly (1)") {
    val game = new Mastermind("RGBYWK", Some("RGBW"))
    game.makeGuess("YKYK")
    assert(game.numCodesRemaining == 256)
    game.makeGuess("RRGG")
    assert(game.numCodesRemaining == 56)
  }

  test("Validate properly accepts consistent guess (1)") {
    val game = new Mastermind("RGBYWK", Some("RGBW"))
    game.makeGuess("YKYK")
    game.makeGuess("RRGG")
    assert(game.validate("WWRG"))
  }

  test("Solved is only true when the final guess is made (1)") {
    val game = new Mastermind("RGBYWK", Some("RGBW"))
    game.makeGuess("YKYK")
    assert(!game.solved)
    game.makeGuess("RRGG")
    assert(!game.solved)
    game.makeGuess("WWRG")
    assert(!game.solved)
    game.makeGuess("RGBW")
  }

  test("10-color game with 7-color code has 1000000 combinations (1)", Slow) {
    val game = new Mastermind("ABCDEFGHIJ", Some("BEADEDI"))
    assert(game.numCodesRemaining == 10000000)
  }
}
