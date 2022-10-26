package cs671.fireworks

class CompositeEffect(val effect: Effect) extends Effect {
  private var fullDuration = effect.duration //fullDuration should update as effects are added

  def duration = fullDuration

  import scala.collection.mutable.{ Map => MutableMap }

  protected val timings: MutableMap[Seconds, List[Effect]] = MutableMap((0.s -> List(effect)))
  private var lastInsertionTime = 0.s
  private var lastDuration = effect.duration

  override def followedBy(other: Effect): CompositeEffect = {
    fullDuration = duration + other.duration
    (timings.contains(lastInsertionTime + lastDuration), lastInsertionTime + lastDuration) match {
      case (true, num) => timings.update(num, other :: timings(num))
      case (false, num) => timings.update(num, List(other))
    }
    lastInsertionTime = lastInsertionTime + lastDuration
    lastDuration = other.duration
    this
  }

  def +(offset: Int): CompositeEffect = {
    val finalOff : ((Seconds, List[Effect])) => (Seconds, List[Effect]) = num =>
      if(num._1.seconds > lastInsertionTime.seconds + lastDuration.seconds)
        ((num._1.seconds + offset).s, num._2 )
      else
        (num._1, num._2)
    timings.map(finalOff)
    this
  }

  override def togetherWith(other: Effect): CompositeEffect = {
    val durationCur : Seconds = other.duration + lastInsertionTime
    if(durationCur.seconds > duration.seconds)
      fullDuration = durationCur
    timings.update(lastInsertionTime, other :: timings(lastInsertionTime))
    lastDuration = other.duration
    this
  }

  override def alongside(other: Effect): CompositeEffect = {
    val durationCur : Seconds = other.duration + lastInsertionTime
    val tim: Seconds = timings.minBy(_._1.seconds)._1
    if(durationCur.seconds > duration.seconds)
      fullDuration = durationCur
    timings.update(tim, other :: timings(tim))
    lastDuration = other.duration
    this
  }

  def !(offset: Int): CompositeEffect = {
    val ex : ((Seconds, List[Effect])) => (Seconds, List[Effect]) = num =>
      if(num._1.seconds > lastInsertionTime.seconds)
        ((num._1.seconds + offset).s, num._2)
      else
        (num._1,num._2)
    timings.map(ex)
    this
  }

  def traverse[A,  E <: Effect](timeOffset: Seconds = 0.s, step: Seconds = 1.s)
                 (visitor: EffectVisitor[A,E]): A = {
    val col = for((start -> effectList) <- timings if(start < timeOffset + step)) yield {
      effectList.collect(visitor.select orElse {case c: CompositeEffect => c}).map(_ match {
        case c: CompositeEffect => c.traverse[A,E](timeOffset - start, step)(visitor)
        case e: E => visitor.apply(e, timeOffset - start,step)
        case x => println(s"WARNING: Reached $x in traverse!"); visitor.identity
      })
    }
    col.flatten.foldLeft(visitor.identity)(visitor.combine(_,_))
  }

  override def render(timeOffset: Seconds = 0.s, step: Seconds = 1.s): String = {
    val col = (
      for((start -> effectList) <- timings if(start < timeOffset + step);
          effect <- effectList if(timeOffset < start + effect.duration)) yield
    effect.render(timeOffset - start, step)
    )
    if(!col.isEmpty) col.reduce(_ concat _) else ""
  }

  def evaluateAt[A](step: Seconds = 1.s, startTime: Seconds = 0.s, endTime: Seconds = duration)
                (format: (Seconds, String) => String = timedHistory)(f: (Seconds, Seconds) => A): Unit = {
    for (t <- startTime to endTime by step.toInt())
      println(format(t.s, f(t.s, step).toString))
  }

  def timedHistory(t: Seconds, event: String):String = (f"{${t.mmss}%7s}\n" + event)

  def play(step: Seconds = 1.s, startTime: Seconds = 0.s, endTime: Seconds = duration): Unit = {
    evaluateAt(step, startTime, endTime) (timedHistory) (render)
  }

  def generateTimings[E <: Effect](offsetFn: (E) => Seconds = (_: E) => 0.s): Map[Seconds, List[Effect]] = {
    val result = MutableMap[Seconds, List[Effect]]()
    generateTimingsHelper(result, 0.s, offsetFn)
    result.toSeq.sortBy(_._1)(Ordering.by(_.seconds)).toMap
  }

  private def generateTimingsHelper[E <: Effect](result: MutableMap[Seconds, List[Effect]], offset: Seconds,
                                                 offsetFn: (E) => Seconds): Unit = {
    for((start, effectList) <- timings) {
      for(eff <- effectList) eff match {
        case c: CompositeEffect => {
          c.generateTimingsHelper(result,offset + start, offsetFn)
        }
        case e: E => {
          if(result.contains(offsetFn(e) + start + offset)) {
            result.update(offsetFn(e) + start + offset, e :: result(offsetFn(e) + start + offset))
          }
          else
            result.update(offsetFn(e) + start + offset, List(e))
        }

      }
    }
  }

}
