import fs2.compression.DeflateParams.Strategy
import cats.effect.IO
import $ivy.`org.typelevel::cats-effect:2.2.0`
import $ivy.`co.fs2::fs2-core:2.4.4`
import $ivy.`co.fs2::fs2-io:2.4.4`

import fs2._
import cats.effect.IO._

// Notes based on Intro to Functional Streams for Scala (FS2)
// https://youtu.be/cahvyadYfX8

//Stream.emits allows us to 'lift' the list here into a pure stream
Stream(1, 2, 3)
  .flatMap(n => Stream.emits(List.fill(n)(n)))

def inc[A](n: Int): Stream[fs2.Pure, Int] =
  Stream(n).flatMap(n => Stream.emits(List.fill(n)(n))) ++ inc(n + 1)

inc(1).take(100).toList

//Something you couldn't do with collections easily

val s = Stream(1, 2, 3)
val t = Stream(4, 5, 6)

val u = s.interleave(t).toList

val s2 = Stream.range(0, 10)
val t2 = s2.intersperse(-42)
t2.toList

//Zip example

val s3 = Stream.range(0, 10)
val t3 = Stream(1, 2, 3)

// Terminates with the length of the shorter stream.
// This pattern of working with infinite streams but bailing early is common
s3.zip(t3).toList
// How to make t3 repeat over and over
s3.zip(t3.repeat).toList

//Deferring until overall IO is evaluated
val io1: IO[(String, Long)] = IO {
  (
    s"Computing value in ${Thread.currentThread().getName()}",
    System.currentTimeMillis
  )
}
io1.unsafeRunSync()

//Repeat eval example
val r1 = Stream.repeatEval(io1).take(10).compile.toList.unsafeRunSync
println(r1)
