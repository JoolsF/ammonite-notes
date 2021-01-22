import java.time.LocalDateTime
import $ivy.`org.typelevel::cats-effect:2.3.1`
import cats.implicits._
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.duration.MILLISECONDS

/*
  EEChapter1
 */

// Exercise 1: Timing
// This is an effect according to the book's definition because:
// 
// 1. We know the effect and the result
// 2. The effect description is seperate from the execution
 case class MyIO[A](unsafeRun: () => A) {

  def map[B](f: A => B): MyIO[B] =
    MyIO(() => f(unsafeRun()))

  def flatMap[B](f: A => MyIO[B]): MyIO[B] =
    MyIO(() => f(unsafeRun()).unsafeRun())
}

object MyIO {

  def putStr(s: => String): MyIO[Unit] =
    MyIO(() => println(s))

}

val clock: MyIO[Long] = MyIO(() => System.currentTimeMillis())

def time[A](action: MyIO[A]): MyIO[(FiniteDuration, A)] = {
  clock.flatMap { startTime =>
    action.flatMap { a =>
      clock.map { endTime =>
        (FiniteDuration(endTime - startTime, MILLISECONDS), a)
      }
    }
  }
}

val timedHello = time(MyIO.putStr("hello"))

timedHello.unsafeRun() match {
  case (duration, _) => println(s"'hello' took $duration")
}
