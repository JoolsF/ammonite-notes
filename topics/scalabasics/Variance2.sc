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