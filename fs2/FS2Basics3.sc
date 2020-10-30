import $ivy.`org.typelevel::cats-effect:2.2.0`
import $ivy.`co.fs2::fs2-core:2.4.4`
import $ivy.`co.fs2::fs2-io:2.4.4`

import cats.effect.{Blocker, ExitCode, IO, IOApp, Resource}
import fs2.{io, text, Stream}
import java.nio.file.Paths

// Notes based on Intro to Functional Streams for Scala (FS2)
// https://youtu.be/cahvyadYfX8

/*
 *Reading from and writing to a file example
 */

val inputFileName =
  java.nio.file.Paths.get("/Users/julienfenner/Desktop/foo.txt")
val outputFileName =
  java.nio.file.Paths.get("/Users/julienfenner/Desktop/foo2.txt")

// Blocker provides an ExecutionContext that is intended for executing blocking tasks and integrates directly with ContextShift.
import scala.concurrent.ExecutionContext.Implicits.global
implicit val cs = IO.contextShift(global)

val fileReadWrite = Stream
  .resource(Blocker[IO])
  .flatMap { blocker =>
    io.file
      .readAll[IO](inputFileName, blocker, 16)
      .through(text.utf8Decode) // transform the stream
      .through(text.lines)
      .filter(_ != "3")
      .intersperse("\n")
      .through(text.utf8Encode)
      .through(io.file.writeAll(outputFileName, blocker))

  }


 