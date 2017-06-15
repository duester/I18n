package test

import scala.reflect.runtime.universe._

import shapeless._
import shapeless.ops.hlist._

sealed trait Color2
case object Red2 extends Color2
case object Green2 extends Color2
case object Blue2 extends Color2

trait Item2 {
  type Colors <: HList
}

case object RedGreenItem2 extends Item2 {
  type Colors = Red2.type :: Green2.type :: HNil
}

case class GreenBlueItemWithSize2(size : Int) extends Item2 {
  type Colors = Green2.type :: Blue2.type :: HNil
}

object Test3 {
  def foo[I <: Item2, C <: Color2 : TypeTag](item : I, color : C)(implicit contains : Selector[I#Colors, C]) : Unit = {
    println(s"item = $item, color = $color")
    println(s"type = ${typeOf[C]}")
  }

  def main(args : Array[String]) : Unit = {
    foo(RedGreenItem2, Green2)
    foo(GreenBlueItemWithSize2(5), Blue2)
  }
}