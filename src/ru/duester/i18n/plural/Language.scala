package ru.duester.i18n.plural

import ru.duester.i18n.plural.category._

trait Language {
  type Category <: PluralCategory
}

object Language {
  type Aux[C <: PluralCategory] = Language { type Category = C }
}

class Root extends Language {
  type Category <: OTHER
}