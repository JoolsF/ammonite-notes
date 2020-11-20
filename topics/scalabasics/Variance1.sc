class Organism()
class Animal() extends Organism
class Cat() extends Animal

//List[Organism]
val organismList = List(new Organism)
//List[Animal]
val animalList = List(new Animal)
//List[Cat]
val catList = List(new Cat)

//List[Organism]
organismList :+ new Organism
//List[Organism]
organismList :+ new Animal
//List[Organism]
organismList :+ new Cat

//List[Organism]
animalList :+ new Organism
//List[Animal]
animalList :+ new Animal
//List[Animal]
animalList :+ new Cat

//List[Organism]
catList :+ new Organism
//List[Animal]
catList :+ new Animal
//List[Cat]
catList :+ new Cat

// add super type to organismList
// List[Object]
organismList :+ new Object 



/*
 If B subtype of A then BoxCovariant[B]is a subtype of BoxCovariant[A].
 BoxCovariant[B] can be substituted for BoxCovariant[A] 
*/
class BoxCovariant[+A] (value: A) {
  def convert[B >: A](b: B) = new BoxCovariant(b)
}

//BoxCovariant[Organism]
val boxCovOrganism = new BoxCovariant(new Organism())
//BoxCovariant[Organism]
boxCovOrganism.convert(new Animal())

val animalBox = boxCovOrganism.convert(new Animal())



/*
 If B subtype of A then BoxConvariant[A] is a subtype of BoxConvariant[B].
 This reverses the sub-typing relationship of A and B
 BoxConvariant[A] cannot be substituted with BoxConvariant[B]
*/
class BoxContravariant[-A] (value: A) {
  // B refers to subtype of A
  def convert[B <: A](b: B) = new BoxContravariant(b)
}

//BoxConvariant[Organism]
val boxConOrganism = new BoxContravariant(new Organism())
//BoxConvariant[Animal]
boxConOrganism.convert(new Animal())

