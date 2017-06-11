package test

import scala.reflect.runtime.universe

import ru.duester.i18n.plural._
import ru.duester.i18n.plural.cardinal._
import ru.duester.i18n.plural.category._

object Test {
  def main(args : Array[String]) : Unit = {
    //val tpe = typeOf[Ru]
    //val m = tag.mirror
    //val cl = m.runtimeClass(tpe)
    //println(getParentTypeSymbols(tpe.typeSymbol))
    val i18n0 = I18nString[Root]("default")
    val i18n1 = I18nString[Ru, Few]("ru_few: %1$s, and once more: %1$s, besides: %2$d")
    val i18n2 = I18nString[En]("en_other")
    val i18n3 : I18nString[I18nLanguages] = i18n0 | i18n1 | i18n2
    println(i18n3)
    println(i18n3[Ru](18))
    println(i18n3[Ru](3, "чуток", 18))
    println(raw"exact:5>привет")
    println(raw"few>привет")
  }
}