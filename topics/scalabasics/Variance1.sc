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
 If B subtype of A then ThingCovariant[B]is a subtype of ThingCovariant[A].
 ThingCovariant[B] can be substituted for ThingCovariant[A] 
*/
class ThingCovariant[+A] (value: A) {
  def convert[B >: A](b: B) = new ThingContravariant(b)
}

//ThingCovariant[Organism]
val thingConOrganism = new ThingCovariant(new Organism())
//ThingCovariant[Organism]
thingConOrganism.convert(new Animal())

/*
 If B subtype of A then ThingCovariant[A] is a subtype of ThingCovariant[B].
 This reverses the sub-typing relationship of A and B
 ThingCovariant[A] cannot be substituted with ThingCovariant[B]
*/
class ThingContravariant[-A] (value: A) {
  // B refers to subtype of A
  def convert[B <: A](b: B) = new ThingContravariant(b)
}

//ThingCovariant[Organism]
val thingCovOrganism = new ThingContravariant(new Organism())
//ThingCovariant[Animal]
thingCovOrganism.convert(new Animal())