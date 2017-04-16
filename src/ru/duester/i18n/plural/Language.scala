package ru.duester.i18n.plural

import ru.duester.i18n.plural.category._

trait Language {
  type Category <: Other
  type Next <: Language
}

trait DefaultLanguageCategory {
  type Category = Other
}
trait DefaultLanguageParent {
  type Next = Root
}
trait DefaultLanguageParameters extends DefaultLanguageCategory with DefaultLanguageParent

class Root extends Language with DefaultLanguageCategory {
  type Next = Nothing
}