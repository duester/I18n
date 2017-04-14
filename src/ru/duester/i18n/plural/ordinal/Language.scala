package ru.duester.i18n.plural.ordinal

import ru.duester.i18n.plural._
import ru.duester.i18n.plural.category._

class En extends Language with DefaultLanguageParent {
  type Category = One with Two with Few with Other
}
class Ru extends Language with DefaultLanguageParameters