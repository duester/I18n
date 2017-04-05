package ru.duester.i18n.plural.category

sealed trait PluralCategory
case class ZERO() extends PluralCategory
case class ONE() extends PluralCategory
case class TWO() extends PluralCategory
case class FEW() extends PluralCategory
case class MANY() extends PluralCategory
case class OTHER() extends PluralCategory