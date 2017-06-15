package ru.duester.i18n.plural

import ru.duester.i18n.plural.category._
import shapeless._

trait Language {
  type Categories <: HList

  val next : Language
  final def category(number : Double) : Exact = categoryNonNeg(math.abs(number))
  protected def categoryNonNeg(number : Double) : Exact
  override def toString() : String = getClass.getSimpleName.toLowerCase()
}

trait DefaultLanguageCategory {
  type Categories = Other.type :: HNil
}

trait DefaultLanguageParent { self : Language =>
  val next : Language = Root
}

trait DefaultLanguageParameters extends DefaultLanguageCategory with DefaultLanguageParent { self : Language =>
}

case object Root extends Language with DefaultLanguageCategory {
  val next : Language = Root
  protected def categoryNonNeg(number : Double) : Exact = new Exact(number)
}