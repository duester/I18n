package ru.duester.i18n.plural.cardinal

import scala.reflect.runtime.universe._

import ru.duester.i18n.plural._
import ru.duester.i18n.plural.category._

class En extends Language with DefaultLanguageParent {
  type Category = ExactZero[Other] with One with Other
}

object En {
  implicit object PluralCategoryToTypeTagEn extends PluralCategoryToTypeTag[En] {
    protected def categoryNonNeg[C >: En#Category <: PluralCategory](number : Double) : TypeTag[C] = number match {
      case 0 => typeTag[ExactZero[Other]].asInstanceOf[TypeTag[C]]
      case 1 => typeTag[One].asInstanceOf[TypeTag[C]]
      case _ => typeTag[Other].asInstanceOf[TypeTag[C]]
    }
  }
}

class Ru extends Language with DefaultLanguageParent {
  type Category = ExactZero[Other] with One with Few with Many with Other
}

object Ru {
  implicit object PluralCategoryToTypeTagRu extends PluralCategoryToTypeTag[Ru] {
    protected def categoryNonNeg[C >: Ru#Category <: PluralCategory](number : Double) : TypeTag[C] = {
      if (number == 0) {
        typeTag[ExactZero[Other]].asInstanceOf[TypeTag[C]]
      }
      else (number % 10) match {
        case 1 if number % 100 != 11 => typeTag[One].asInstanceOf[TypeTag[C]]
        case 2 | 3 | 4 if number % 100 < 12 || number % 100 > 14 => typeTag[Few].asInstanceOf[TypeTag[C]]
        case _ if number.isWhole() => typeTag[Many].asInstanceOf[TypeTag[C]]
        case _ => typeTag[Other].asInstanceOf[TypeTag[C]]
      }
    }
  }
}