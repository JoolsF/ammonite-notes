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
 If B subtype of A then BoxCovariant[B] is a subtype of BoxCovariant[A].
 BoxCovariant[B] can be substituted for BoxCovariant[A] 

 A is Covariant meaning that for any 'B <: A' Box[B] can be seen as Box[A]

 Therefore in convert we must set [B >: A] to refer to the supertype

*/
class BoxCovariant[+A] (value: A) {
  // Because of Scala type inference we can pass in a B that is subtype of A and it will use A's type.
  // It does not mean that B must be a super type of A.  Compiler will find the common super type.
  def convert[B >: A](b: B) = new BoxCovariant(b)
}

//BoxCovariant[Organism]
val boxCovOrganism = new BoxCovariant(new Organism())
//BoxCovariant[Organism]
boxCovOrganism.convert(new Animal())

//A problem with the above

class Dog() extends Animal

val boxCovCat = new BoxCovariant(new Cat())
val boxAnimal: BoxCovariant[Animal] = boxCovCat
// We've put a dog in a cat box!
val dogBox = boxAnimal.convert(new Dog())


/*
 If B subtype of A then BoxConvariant[A] is a subtype of BoxConvariant[B].
 This reverses the sub-typing relationship of A and B
 BoxConvariant[A] cannot be substituted with BoxConvariant[B]
*/
class BoxContravariant[-A] (value: A) {
  //Compiler
  def convert[B <: A](b: B) = new BoxContravariant(b)
}

//BoxConvariant[Organism]
val boxConOrganism = new BoxContravariant(new Organism())
//BoxConvariant[Animal]
boxConOrganism.convert(new Animal())

