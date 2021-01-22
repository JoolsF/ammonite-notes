import $ivy.`org.typelevel::cats-effect:2.2.0`
import $ivy.`co.fs2::fs2-core:2.4.4`

import fs2.Stream
import cats.effect.IO._
import cats.effect.IO

sealed abstract class Animal
case class Cat() extends Animal
case class Dog() extends Animal

case class Box[+A](value: A) {

  def set[B >: A](a: B): Box[B] = Box(a)

}


val catBox = Box[Cat]((Cat()))
val animalBox: Box[Animal] = catBox // valid because `Cat <: Animal`
val dog = new Dog
animalBox.set(dog) // This is non-sensical!

val s = Stream.eval(IO("hello"))

val ss  = s.map(_ * 2)

