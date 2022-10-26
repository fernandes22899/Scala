package cs671.mastermind
import scala.util.Random

/** The following restrictions on constructor arguments must
  * be enforced:
  *  - color list must contain between 2 and 10 colors.
  *  - code must be between 2 and 7 characters long.
  */
final class Mastermind(colors: String, code: Option[String]) {

 def this(colors : String) = this(colors, None)
 var pos : List[String] = List()
 var nGuess : String = ""
 var codeString = code.toString
 val opt : Int = colors.length



 //checking lengths
 /*
 if( colors.length < 2 || colors.length > 10){
  throw new IllegalArgumentException
 }
 if( codeString.length < 2 || codeString.length > 7 ){
  throw new IllegalArgumentException
 }*/
 require(opt >= 2 && opt <=10, "Color list does not match reqs.")
 require(codeGuess >= 2 && codeGuess <= 7,"code length does not meet requirement.")

 var genPos : List[String] = generateAllPossibilities
 def numberOfPos() : Int = genPos.length

 /**
    * Generates a list of all possible guesses.
    * You may change this to return List[List[Char]] if you prefer.
    */
  def generateAllPossibilities : List[String] = {
   genPos = List()
   def possibleRec( cols : String, guess : String, amCols : Int, leng : Int ) : Unit = {
    if(leng == 0)  genPos = guess :: genPos
    for(num <- 0 until amCols)  possibleRec(cols, guess + cols(num), amCols, leng - 1)
   }
   possibleRec(colors, "", opt, codeGuess)
   genPos
  }

  /**
    * Returns how many possible codes remain, given the guesses already made.
    */
  def numCodesRemaining : Int = numberOfPos

  /**
    * Determine if the passed guess is valid given the guesses made already.
    * Returns true if valid, or false if contradicted by evidence from prior
    * guesses.
    */
  def validate(guess: String): Boolean = !(pos contains guess)
  
  /**
    * Return the evaluation string for the passed guess and secret code.
    * The return value contains an 'O' character
    * for every color in the guess that is in the right place in the code, and
    * an 'X' character for every color in the guess that is not in the right
    * place, but is present in the code. All 'O' characters come before any 'X'
    * characters.
    */
  private def evaluateGuess(guess: String, code: String) = {

  }

  /**
  * Return the evaluation string for the passed guess without adding it
  * to the list of guesses made. The return value contains an 'O' character
  * for every color in the guess that is in the right place in the code, and
  * an 'X' character for every color in the guess that is not in the right
  * place, but is present in the code. All 'O' characters come before any 'X'
  * characters.
  */
  def evaluateGuess(guess : String): String = evaluateGuess(guess,nGuess)

  /**
    * Add the passed guess to the lists of guesses made already and returns
    * the result of evaluateGuess (one-argument version) on the passed guess
    */
  def makeGuess(guess: String): String = {

  }
  
  /**
    * Randomly generate a guess from current possibilities
    */
  def generateGuess: String = {

  }
    
  /**
    * Return the percentage of valid guesses that the passed guess would
    * eliminate in the worst case, given the list of guesses made already.
    * This must be done by evaluating the guess against each code in the
    * current list of possible secret codes.
    */
  def analyzeGuess(guess : String): Double = {

  }

 /**
    * Generate best guess from current possibilities based
    * on the percentage of guesses it would eliminate
    */
  def generateBestGuess: String = {

  }

  /**
    * Returns true if one of the guesses matches the code, false otherwise
    */
  def solved : Boolean = pos contains code
}
