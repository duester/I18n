package ru.duester.i18n.plural.ordinal

import scala.reflect.runtime.universe._

import ru.duester.i18n.plural._
import ru.duester.i18n.plural.category._

class En extends Language with DefaultLanguageParent {
  type Category = One with Two with Few with Other
}

object En {
  implicit object PluralCategoryToTypeTagEn extends PluralCategoryToTypeTag[En] {
    protected def categoryNonNeg[C >: En#Category <: PluralCategory](number : Double) : TypeTag[C] = (number.toInt % 10) match {
      case 1 if number % 100 != 11 => typeTag[One].asInstanceOf[TypeTag[C]]
      case 2 if number % 100 != 12 => typeTag[Two].asInstanceOf[TypeTag[C]]
      case 3 if number % 100 != 13 => typeTag[Few].asInstanceOf[TypeTag[C]]
      case _                       => typeTag[Other].asInstanceOf[TypeTag[C]]
    }
  }
}

class Ru extends Language with DefaultLanguageParameters

object Ru {
  implicit object PluralCategoryToTypeTagRu extends PluralCategoryToTypeTag[Ru] {
    protected def categoryNonNeg[C >: Ru#Category <: PluralCategory](number : Double) : TypeTag[C] = typeTag[Other].asInstanceOf[TypeTag[C]]
  }
}