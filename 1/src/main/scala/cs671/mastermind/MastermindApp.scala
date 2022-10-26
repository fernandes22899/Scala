package cs671.mastermind

import scala.annotation.tailrec

/** Command-line application.  Requires a string of possible colors first
  * (such as "RGBYWK") followed by an optional starting code. If no code
  * is given, a random one is chosen
  */
object MastermindApp {

  private def usage() =
      println("Usage: MastermindApp <colorString> [<start-code>]")

  private var slots = 4
  private var colors: String = ""

  private def guessPrompt = s"Enter guesses of length ${slots} using any " +
                            s"combination of your colors from ${colors}," +
                            " or enter an empty guess> "
  private def cmdPrompt(s: Double) =
                      s"This guess reduces possible solutions by at least ${s}%\n" +
                      "Do you want to apply this guess? "

  private def readln(x:String) = scala.io.StdIn.readLine(x)

  private def play(game: Mastermind): Unit = {
    def getGuess: Option[String] = readln(guessPrompt) match {
      case guess: String if guess.length == slots => Some(guess)
      case _      => None
    }

    def yesOrNo(line: String): Option[Boolean] = line.trim match {
      case "y" | "Y" | "yes" | "Yes" | "YES" => Some(true)
      case "n" | "N" | "no" | "No" | "NO"    => Some(false)
      case _                                 => None
    }

    @tailrec
    def getChoice(strength: Double): Boolean = {
      yesOrNo(readln(cmdPrompt(strength))) match {
        case Some(ans) => ans
        case None      => getChoice(strength)
     }
    }

    while (!game.solved) {
      println(s"There are currently ${game.numCodesRemaining} possibilities.")
      val guess = getGuess match {
        case Some(g: String) => g match {
          case "quit" | "q" | "Q" | "X" | "x" => println ("User exits.\n"); return
          case _ => g
        }
        case _ => { val g = game.generateGuess
                    println("Generated guess: " + g)
                    g
        }
      }
      if (game.validate(guess)) {
        if (getChoice(game.analyzeGuess(guess))) {
          println("Result: " + game.makeGuess(guess))
        }
      } else {
        println("This guess is already invalid based on your prior guesses!")
      }
    }
    println("Done")
  }

  /** Usage: `MastermindApp <colorString> [<start-code>]` */
  def main(args: Array[String]): Unit = {
    if (args.length < 1 || args.length > 2) usage()
    else {
      colors = args(0);
      val code: Option[String] = if (args.length == 2) {slots = args(1).length; Some(args(1))} else None
      val game = new Mastermind(args(0), code)
      play(game)
    }
  }
}
