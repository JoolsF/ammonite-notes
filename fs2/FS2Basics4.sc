import $ivy.`org.typelevel::cats-effect:2.2.0`
import $ivy.`co.fs2::fs2-core:2.4.4`

import fs2.Stream
import fs2._

/*
 * Exercises from https://fs2.io/guide.html
 */

// Exercises Stream Building - implement repeat
def repeat[F[_], O](s: Stream[F, O]): Stream[F, O] =
  s ++ repeat(s)

// Exercises Stream Building - implement drain
def drain[F[_], O](s: Stream[F, O]): Stream[F, O] =
  s >> Stream.empty

// Exercises Stream Transforming - implement takeWhile
//
def takeWhile[F[_], O](s: Stream[F, O], cond: O => Boolean) = {
  def go(s: Stream[F, O]): Pull[F, O, Unit] = {
    s.pull.uncons.flatMap {
      case Some((hd, tl)) =>
        hd.forall(cond) match {
          // i.e. all element in the chunk return true for cond
          case true =>
            Pull.output(hd.take(hd.size)) >> go(tl)
          case false =>
            hd.indexWhere(cond) match {
              case Some(i) => Pull.output(hd.take(i)) >> Pull.done
              case None    => Pull.done
            }
        }
      case None => Pull.done
    }
  }
  go(s).stream
}

// val cond: Int => Boolean = _ < 20
// takeWhile(Stream.range(1, 100), cond)

// Exercises Stream Transforming - implement intersperse
def intersperse[F[_], O](s: Stream[F, O], e: O): Stream[F, O] =
  s.flatMap(x => Stream.empty)

println(intersperse(Stream.range(1,10), 0).toList)
