package ru.duester.i18n.plural

import ru.duester.i18n.plural.category._

trait Language[Cat <: PluralCategory] {

}

object DEFAULT extends Language[OTHER]