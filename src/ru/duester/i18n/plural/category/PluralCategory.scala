package ru.duester.i18n.plural.category

sealed trait PluralCategory {
  type Next <: PluralCategory

  override def toString : String = super.getClass.getSimpleName.toLowerCase()
}

trait DefaultNextCategory {
  type Next = Other
}

case class ExactZero[T <: PluralCategory]() extends PluralCategory {
  type Next = T

  override def toString : String = "0"
}
class Zero extends PluralCategory with DefaultNextCategory
class One extends PluralCategory with DefaultNextCategory
class Two extends PluralCategory with DefaultNextCategory
class Few extends PluralCategory with DefaultNextCategory
class Many extends PluralCategory with DefaultNextCategory
class Other extends PluralCategory {
  type Next = Nothing
}