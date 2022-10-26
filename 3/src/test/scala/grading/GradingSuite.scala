package grading

import java.util.concurrent.ThreadFactory

import edu.unh.cs.mc.grading._
import edu.unh.cs.mc.utils.threads.StoppableThread
import edu.unh.cs.mc.utils.threads.StoppableThread.Policies.{ async, waitAndStop }
import edu.unh.cs.mc.utils.threads.StoppableThread.StopPolicy
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.concurrent.ThreadSignaler
import org.scalatest.time.SpanSugar._

import scala.concurrent.ExecutionContext.Implicits.global

trait GradingSuite extends AnyFunSuite
  with DualTimeLimits with RunnerFactory with NoStackOverflowError with Grading {

  override val defaultTestSignaler = ThreadSignaler
  implicit val stopPolicy: StopPolicy = async(waitAndStop(5.0))

  val runnerFactory: ThreadFactory = task => new StoppableThread(task)

  val shortTimeLimit = 1.second
  val longTimeLimit = 10.seconds
}
