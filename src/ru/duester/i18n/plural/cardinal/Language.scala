package ru.duester.i18n.plural.cardinal

import scala.reflect.runtime.universe._

import ru.duester.i18n.plural._
import ru.duester.i18n.plural.category._

case object En extends Language with DefaultLanguageParent {
  type Category = Exact.type with One.type with Other.type

  protected def categoryNonNeg(number : Double) : Exact = number match {
    case 1 => new Exact(number) {
      val next : PluralCategory = One
    }
    case _ => new Exact(number) {
      val next : PluralCategory = Other
    }
  }
}

case object Ru extends Language with DefaultLanguageParent {
  type Category = Exact.type with One.type with Few.type with Many.type with Other.type

  protected def categoryNonNeg(number : Double) : Exact = (number % 10) match {
    case 1 if number % 100 != 11 => new Exact(number) {
      val next : PluralCategory = One
    }
    case 2 | 3 | 4 if number % 100 < 12 || number % 100 > 14 => new Exact(number) {
      val next : PluralCategory = Few
    }
    case _ if number.isWhole() => new Exact(number) {
      val next : PluralCategory = Many
    }
    case _ => new Exact(number) {
      val next : PluralCategory = Other
    }
  }
}