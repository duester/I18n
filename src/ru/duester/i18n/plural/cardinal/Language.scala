package ru.duester.i18n.plural.cardinal

import ru.duester.i18n.plural._
import ru.duester.i18n.plural.category._

case object En extends Language with DefaultLanguageParameters {
  implicit object OneC extends AcceptableCategory[One.type]

  protected def categoryNonNeg(number : Double) : Exact = number match {
    case 1 => new Exact(number) {
      override val next : PluralCategory = One
    }
    case _ => new Exact(number)
  }
}

case object Ru extends Language with DefaultLanguageParameters {
  implicit object OneC extends AcceptableCategory[One.type]
  implicit object FewC extends AcceptableCategory[Few.type]
  implicit object ManyC extends AcceptableCategory[Many.type]

  protected def categoryNonNeg(number : Double) : Exact = (number % 10) match {
    case 1 if number % 100 != 11 => new Exact(number) {
      override val next : PluralCategory = One
    }
    case 2 | 3 | 4 if number % 100 < 12 || number % 100 > 14 => new Exact(number) {
      override val next : PluralCategory = Few
    }
    case _ if number.isWhole() => new Exact(number) {
      override val next : PluralCategory = Many
    }
    case _ => new Exact(number)
  }
}