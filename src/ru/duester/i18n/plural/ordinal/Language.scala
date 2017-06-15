package ru.duester.i18n.plural.ordinal

import ru.duester.i18n.plural._
import ru.duester.i18n.plural.category._
import shapeless._

case object En extends Language with DefaultLanguageParent {
  type Categories = One.type :: Two.type :: Few.type :: Other.type :: HNil

  protected def categoryNonNeg(number : Double) : Exact = (number.toInt % 10) match {
    case 1 if number % 100 != 11 => new Exact(number) {
      override val next : PluralCategory = One
    }
    case 2 if number % 100 != 12 => new Exact(number) {
      override val next : PluralCategory = Two
    }
    case 3 if number % 100 != 13 => new Exact(number) {
      override val next : PluralCategory = Few
    }
    case _ => new Exact(number)
  }
}

case object Ru extends Language with DefaultLanguageParameters {
  protected def categoryNonNeg(number : Double) : Exact = new Exact(number)
}