package ru.duester.i18n.plural.category

sealed trait PluralCategory {
  val next : PluralCategory
  override def toString : String = getClass.getSimpleName.toLowerCase()
}

trait DefaultNextCategory { self : PluralCategory =>
  val next : PluralCategory = Other
}

case class Exact(val number : Double) extends PluralCategory with DefaultNextCategory {
  override def toString : String = number.toString()
}

case object Zero extends PluralCategory with DefaultNextCategory

case object One extends PluralCategory with DefaultNextCategory

case object Two extends PluralCategory with DefaultNextCategory

case object Few extends PluralCategory with DefaultNextCategory

case object Many extends PluralCategory with DefaultNextCategory

case object Other extends PluralCategory with DefaultNextCategory