package ru.duester.i18n.plural

import scala.reflect.runtime.universe._

import ru.duester.i18n.plural.category._

trait Language {
  type Category <: Other.type

  val next : Language
  final def category(number : Double) : Exact = categoryNonNeg(math.abs(number))
  protected def categoryNonNeg(number : Double) : Exact
  override def toString() : String = super.getClass.getSimpleName
}

trait DefaultLanguageCategory {
  type Category = Other.type
}

trait DefaultLanguageParent { self : Language =>
  val next : Language = Root
}

trait DefaultLanguageParameters extends DefaultLanguageCategory with DefaultLanguageParent { self : Language =>
}

case object Root extends Language with DefaultLanguageCategory {
  val next : Language = Root
  protected def categoryNonNeg(number : Double) : Exact = new Exact(number) {
    val next : PluralCategory = Other
  }
}