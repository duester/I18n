package ru.duester.i18n.plural.cardinal

import ru.duester.i18n.plural._
import ru.duester.i18n.plural.category._

class En extends Language with DefaultLanguageParameters
class Ru extends Language with DefaultLanguageParent {
  type Category = One with Few with Many with Other
}