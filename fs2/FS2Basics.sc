import $ivy.`org.typelevel::cats-effect:2.2.0`
import $ivy.`co.fs2::fs2-core:2.4.4`

import fs2.Stream

/*
 * A Stream[F,O] is a stream of O values which may request evaluation of F effects
 */

// This has the effect type Pure which requires no evaluation of effects to produce a result
// Its a 'pure stream'
val s1 = Stream.emit(1) //val s1: fs2.Stream[[x]fs2.Pure[x],Int] = Stream(..)
s1.toList //List(1)

// Has methods similar to list e.g concat, map, fold, filter
(Stream(1, 2, 3) ++ Stream(4, 5, 6)).toList //List(1, 2, 3, 4, 5, 6)
Stream(1, 2, 3).flatMap(i => Stream(i, i)).toList // List(1, 1, 2, 2, 3, 3)

// and other less familiar methods
Stream
  .range(0, 5)
  .intersperse(42)
  .toList //List(0, 42, 1, 42, 2, 42, 3, 42, 4)

Stream(1, 2, 3).repeat.take(9).toList //List(1, 2, 3, 1, 2, 3, 1, 2, 3)

Stream(1, 2, 3).repeatN(2).take(9).toList // List(1, 2, 3, 1, 2, 3)

/*
 * Evaluating effects
 */
import cats.effect.IO

val eff: Stream[IO, Int] =
  Stream.eval(
    IO(1)
  ) // fs2.Stream[cats.effect.IO,Unit] = Stream(..)

//Compile to single effect. When this toVector has returned, the stream has not begun execution -- this method simply
//compiles the stream down to the target effect type.
val effCompiled: IO[List[Int]] =
  eff.compile.toList // cats.effect.IO[Vector[Unit]]

val eff1Res: Seq[Int] = effCompiled.unsafeRunSync() //List(1)

//For evaluating effects then drain is what would be used

val eff2: Stream[IO, Unit] = Stream.eval(
  IO(println(2))
)
val eff2Res: Unit = eff2.compile.drain.unsafeRunSync //2
