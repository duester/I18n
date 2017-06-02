package ru.duester.i18n.plural.category

sealed trait PluralCategory {
  type Next <: PluralCategory

  override def toString : String = super.getClass.getSimpleName
}

trait DefaultNextCategory {
  type Next = Other
}

case class Exact(val number : Double) extends PluralCategory
case class Zero() extends PluralCategory with DefaultNextCategory
case class One() extends PluralCategory with DefaultNextCategory
case class Two() extends PluralCategory with DefaultNextCategory
case class Few() extends PluralCategory with DefaultNextCategory
case class Many() extends PluralCategory with DefaultNextCategory
case class Other() extends PluralCategory {
  type Next = Nothing
}