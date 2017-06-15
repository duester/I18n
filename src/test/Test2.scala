package test

import scala.reflect.runtime.universe._

sealed trait Color
case object Red extends Color
case object Green extends Color
case object Blue extends Color

trait Item {
  trait Acceptable[C <: Color]
}

case object RedGreenItem extends Item {
  implicit object R extends Acceptable[Red.type]
  implicit object G extends Acceptable[Green.type]
}

object Test2 {
  def foo[I <: Item, C <: Color : I#Acceptable : TypeTag](item : I, color : C) : Unit = {
    println(s"item = $item, color = $color")
    println(s"type = ${typeOf[C]}")
  }

  def main(args : Array[String]) : Unit = {
    foo(RedGreenItem, Green)
    Test3.foo(RedGreenItem2, Green2)
  }
}