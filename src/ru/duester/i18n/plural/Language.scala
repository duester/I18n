package ru.duester.i18n.plural

import scala.annotation.implicitNotFound

import ru.duester.i18n.plural.category._

trait Language {
  @implicitNotFound("Category ${C} not acceptable for this language.")
  trait AcceptableCategory[C <: PluralCategory]

  val next : Language
  final def category(number : Double) : Exact = categoryNonNeg(math.abs(number))
  protected def categoryNonNeg(number : Double) : Exact
  override def toString() : String = getClass.getSimpleName.toLowerCase()
}

trait DefaultLanguageCategory { self : Language =>
  implicit object ExactC extends AcceptableCategory[Exact]
  implicit object OtherC extends AcceptableCategory[Other.type]
}

trait DefaultLanguageParent { self : Language =>
  val next : Language = Root
}

trait DefaultLanguageParameters extends DefaultLanguageCategory with DefaultLanguageParent { self : Language =>
}

case object Root extends Language with DefaultLanguageParameters {
  protected def categoryNonNeg(number : Double) : Exact = new Exact(number)
}