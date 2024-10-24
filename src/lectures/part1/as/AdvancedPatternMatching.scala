package lectures.part1.as

import lectures.part1.as.AdvancedPatternMatching.Person

import scala.runtime.Nothing$

object AdvancedPatternMatching extends App{

  val numbers = List(1)
  val description = numbers match {
    case head :: Nil => println(s"the only element is $head") // equivalent  ::(head, Nil)
    case _ =>
  }

  /*
    - constants
    - wildcards
    - case classes
    - tuples
    - some special magic like above
   */

  class Person(val name: String, val age: Int)

  object Person{
    def unapply(person: Person): Option[(String, Int)] = {
      if (person.age < 21) None
      Some((person.name, person.age))
    }

    def unapply(age: Int): Option[String] =
      Some(if (age < 21) "minor" else "major")
  }

  val bob =  new Person("Bob", 25)
  val greeting = bob match {
    case Person(n, a) => s"Hi my name is $n and I am $a yo" // Person(..) <- yo Person wala part chai singleton object sanga name milnu paryo not the argument of unapply fxn
  }

  println(greeting)

  val legalStatus = bob.age match { // yo part chai argument unapply(x:...) sanga match garnu paryo
    case Person(status) => s"My legal Status is $status"
  }

  println(legalStatus)

  /*
  Exercise
   */

  val n: Int = 8
  object singleDigit { // **NOTE: esto pattern matching ma object name starts with LOWERCASE(convention)
    def unapply(arg: Int): Boolean = arg > -10 && arg < 10
  }

  object even{
    def unapply(arg: Int): Boolean = arg % 2 ==0
  }

  val mathProperty = n match {
    case singleDigit()=> "single digit"
    case even() =>"Even number"
    case __ => "no property"
  }

  println(mathProperty)

  //infix patterns
  case class Or[A, B](a: A, b: B) // Either
  val either  = Or(2, "two")
  val humanDescription = either match {
    case number Or string => s" $number is written as  $string"
  }
  println(humanDescription)

  // decomposing sequences
  val vararg = numbers match {
    case List(1, _*) => "starting with 1"
  }

  abstract class MyList[+A] {
    def head: A = ???
    def tail: MyList[A] = ???
  }
  case object Empty extends MyList[Nothing]
  case class Cons[+A](override val head: A, override val tail: MyList[A]) extends MyList[A]

  object MyList {
    def unapplySeq[A](list: MyList[A]): Option[Seq[A]] = {  //unapplySeq like "unapply"
      if (list == Empty) Some(Seq.empty)
      else unapplySeq(list.tail).map(list.head +: _ )
    }
  }

  val myList: MyList[Int] = Cons(1, Cons(2, Cons(3, Empty)))
  val decomposed = myList match {
    case MyList(1, 2, _*) => "starting with 1, 2"
    case _ =>"something else"
  }
  println(decomposed)

  //custom return types for unapply
  //isEmpty: Boolean, get: something.

  abstract class Wrapper[T] {
    def isEmpty: Boolean
    def get: T
  }

  object PersonWrapper {
    def unapply(person: Person): Wrapper[String] = new Wrapper[String] { //unapply can return any type as long as "isEmpty" & "get" implemented
      def isEmpty = false
      def get = person.name
    }
  }
  println(bob match {
    case PersonWrapper(n) => s"This person's name is $n"
    case _ => "an Alien"
  })
}
