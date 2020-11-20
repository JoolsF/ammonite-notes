import $ivy.`org.typelevel::cats-effect:2.2.0`
import $ivy.`co.fs2::fs2-core:2.4.4`

import fs2.Stream

/*
 * A Stream[F,O] is a stream of O values which may request evaluation of F effects
 */

// This has the effect type Pure which requires no evaluation of effects to produce a result
// Its a 'pure stream'
val s1 = Stream.emit(1) //val s1: fs2.Stream[[x]fs2.Pure[x],Int] = Stream(..)
s1.toList

// Has methods similar to list e.g concat, map, fold, filter
(Stream(1, 2, 3) ++ Stream(4, 5, 6)).toList
Stream(1, 2, 3).flatMap(i => Stream(i, i)).toList

// and other less familiar methods
Stream
  .range(0, 5)
  .intersperse(42)
  .toList

Stream(1, 2, 3).repeat.take(9).toList

Stream(1, 2, 3).repeatN(2).take(9).toList

/*
 * Evaluating effects
 */
import cats.effect.IO

val eff: Stream[IO, Int] =
  Stream.eval(
    IO(1)
  ) // fs2.Stream[cats.effect.IO,Unit] = Stream(..)

//Compile to single effect. When this toList has returned, the stream has not begun execution -- this method simply
//compiles the stream down to the target effect type.
val effCompiled: IO[List[Int]] =
  eff.compile.toList

val eff1Res: Seq[Int] = effCompiled.unsafeRunSync()

//For evaluating effects then drain is what would be used

val eff2: Stream[IO, Unit] = Stream.eval(
  IO(println(2))
)
val eff2Res: Unit = eff2.compile.drain.unsafeRunSync

/*
 * Error handling
 */

val err = Stream.raiseError[IO](new Exception("boom"))
val err2 = Stream(1, 2, 3) ++ (throw new Exception("bang"))
val err3 = Stream.eval(IO(throw new Exception("crash")))

// // handleErrorWith is invoked when a stream terminates but its not to be used for cleanup.  Use bracket instead
// // To deal with failures of the effect F it would need to handle there to allow stream continuation
err
  .handleErrorWith { e => Stream.emit(e.getMessage) }
  .compile
  .toList
  .unsafeRunSync()

/*
 * Resource acquisition
 */

val count = new java.util.concurrent.atomic.AtomicLong(0)
val acquire = IO { println("incremented: " + count.incrementAndGet); () }
val release = IO { println("decremented: " + count.decrementAndGet); () }
//Will print incremented and decremented when run
Stream.bracket(acquire)(_ => release).flatMap(_ => Stream(1,2,3) ++ err).compile.drain//.unsafeRunSync()