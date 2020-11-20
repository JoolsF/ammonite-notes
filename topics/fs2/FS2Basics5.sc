import $ivy.`org.typelevel::cats-effect:2.2.0`
import $ivy.`co.fs2::fs2-core:2.4.4`

import fs2.Stream
import cats.effect.Timer
import cats.effect.IO._
import cats.effect.IO
import fs2.Pipe
import scala.concurrent.ExecutionContext.Implicits.global

implicit val timer = IO.timer(global)

/*
 * Pipe examples
 */
def transform: Pipe[IO, String, Int] =
  in => in.map(_.toInt)

Stream.eval(IO("1"))
  .through(transform)
  .take(1)
  .compile
  .toList.unsafeRunSync()