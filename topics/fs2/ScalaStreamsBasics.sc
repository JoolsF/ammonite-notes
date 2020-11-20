/*
 * Why streams / lazy lists?
 *   Functional style - Writing code that describes what we want to do to a sequence, not actually 'doing it' as well.
 *   Evaluation is totally separate.
 *   Efficiency - Laziness has a lot of useful properties e.g. not computing things until required / only computing
 *   what is needed
 *   Declarative style - can express certain concepts more easily e.g. take element until x is true is easy to write
 *   Modelling concept of a stream - working with a never ending sequence e.g. stream of messages, and doing something
 *   with each one
 */

def infiniteStream(number: Int = 1): Stream[Int] =
  number #:: infiniteStream(number + 1)

val lazyList: Seq[Int] =
  infiniteStream().take(
    5
  ) //This is still not evaluated its just 1 + uncomputed tail
val eagerList = List(1, 2, 3, 4, 5)

def identity(i: Int): Int = {
  print(i + " ")
  i
}

val lazyListMapped: Seq[Int] =
  lazyList
    .map(identity)
    .map(identity) //LazyList(<not computed>) still building up computation

// prints 1 2 3 4 5 1 2 3 4 5 i.e does whole sequence in first map then whole sequence in second map
val normalListMapped: Seq[Int] = eagerList.map(identity).map(identity)

//prints 1 1 2 2 3 3 4 4 5 5 i.e applies both maps 'per' element
lazyListMapped.toList

def times2(i: Int): Int = {
  print(i + " ")
  i * 2
}

// lazy version ones computed what it required whilst eager list has to compute everything
lazyList
  .map(times2)
  .takeWhile(_ < 5)
  .toList //1 2 3 val res1: List[Int] = List(2, 4)
normalListMapped
  .map(times2)
  .filter(_ < 5) // 1 2 3 4 5 val res2: List[Int] = List(2, 4)
