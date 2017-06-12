package ru.duester.i18n.plural.ordinal

import scala.reflect.runtime.universe._

import ru.duester.i18n.plural._
import ru.duester.i18n.plural.category._

case object En extends Language with DefaultLanguageParent {
  type Category = One.type with Two.type with Few.type with Other.type

  protected def categoryNonNeg(number : Double) : Exact = (number.toInt % 10) match {
    case 1 if number % 100 != 11 => new Exact(number) {
      val next : PluralCategory = One
    }
    case 2 if number % 100 != 12 => new Exact(number) {
      val next : PluralCategory = Two
    }
    case 3 if number % 100 != 13 => new Exact(number) {
      val next : PluralCategory = Few
    }
    case _ => new Exact(number) {
      val next : PluralCategory = Other
    }
  }
}

case object Ru extends Language with DefaultLanguageParameters {
  protected def categoryNonNeg(number : Double) : Exact = new Exact(number) {
    val next : PluralCategory = Other
  }
}