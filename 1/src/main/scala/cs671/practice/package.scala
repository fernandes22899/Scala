package cs671

import scala.util.Random

package object practice {
  def find42(rand: Random): Int = {
    var count = 1
    while (rand.nextInt != 42) {
      count = count + 1
    }
    count
  }

  def findBig(rand: Random): Int = {
    var big = 0
    do {
      big = rand.nextInt
    } while (big <= 1000000)
    big
  }

  def removeLongerThan(list: List[String], n: Int): List[String] = {
    for(item <- list if item.length() <= n) yield item
  }

  def makePairs(list: List[String]): List[(String, Int)] = {
    for(item <- list) yield (item,item.length)
  }

  def nextLetter(rand: Random): Char = {
    var c = rand.nextInt(26) + 65
    c.toChar
  }

  def makeLetters(rand: Random, n: Int): List[Char] = {
    var list : List[Char] = List()
    for(num <- 1 to n){
      list = list :+ nextLetter(rand)
    }
    list
  }

  def getIfValid[A](ref: CountingRef[A]): Option[A] = {
    try {
      Option(ref.get())
    } catch{
      case _: IllegalStateException => None
    }
  }

  def makeRefs[A](list: List[A]): List[CountingRef[A]] = {
    for(num <- list) yield new CountingRefImpl(num)
  }
  def getRefs[A](list: List[CountingRef[A]]): List[A] = {
    for(num <- list) yield num.get()
  }
  def getValidRefs[A](list: List[CountingRef[A]]): List[A] = {
    for(num <- list if getIfValid(num) != None) yield getIfValid(num).get
  }
}
