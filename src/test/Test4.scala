package test

import scala.reflect.runtime.universe._

import shapeless._
import shapeless.ops.hlist._

object Union {
  type ¬[A] = A => Nothing
  type ¬¬[A] = ¬[¬[A]]
  type ⋁[A, B] = ¬[¬[A] with ¬[B]]
  type |⋁|[A, B] = { type λ[X] = ¬¬[X] <:< (A ⋁ B) }
}

sealed trait Color3
case object Red3 extends Color3
case object Green3 extends Color3
case object Blue3 extends Color3

trait Item3[C <: HList]

case object RedGreenItem3 extends Item3[Red3.type :: Green3.type :: HNil]

case class GreenBlueItemWithSize3(size : Int) extends Item3[Green3.type :: Blue3.type :: HNil]

object Test4 {
  /*def size[T : (Int |⋁| String)#λ](t : T) = t match {
    case i : Int    => i
    case s : String => s.length
  }*/

}