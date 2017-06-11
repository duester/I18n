package ru.duester.i18n.plural

import scala.reflect.runtime.universe._

import ru.duester.i18n.plural.category._

trait Language {
  type Category <: Other
  type Next <: Language

  override def toString() : String = super.getClass.getSimpleName
}

trait DefaultLanguageCategory {
  type Category = Other
}

trait DefaultLanguageParent {
  type Next = Root
}

trait DefaultLanguageParameters extends DefaultLanguageCategory with DefaultLanguageParent

trait PluralCategoryToTypeTag[L <: Language] {
  final def category[C >: L#Category <: PluralCategory](number : Double) : TypeTag[C] = categoryNonNeg(math.abs(number))
  protected def categoryNonNeg[C >: L#Category <: PluralCategory](number : Double) : TypeTag[C]
}

class Root extends Language with DefaultLanguageCategory {
  type Next = Nothing
}

object Root {
  implicit object PluralCategoryToTypeTagRoot extends PluralCategoryToTypeTag[Root] {
    protected def categoryNonNeg[C >: Root#Category <: PluralCategory](number : Double) : TypeTag[C] = typeTag[Other].asInstanceOf[TypeTag[C]]
  }
}