package test

import ru.duester.i18n.plural._
import ru.duester.i18n.plural.cardinal._
import ru.duester.i18n.plural.category._
import shapeless._
import shapeless.ops.hlist._

object Test {
  def main(args : Array[String]) : Unit = {
    //val tpe = typeOf[Ru]
    //val m = tag.mirror
    //val cl = m.runtimeClass(tpe)
    /*val i18n0 = I18nString(Root)("default")
    val i18n1 = I18nString(Ru, Few)("ru_few: %1$s, and once more: %1$s, besides: %2$d")
    val i18n2 = I18nString(Ru)(s"en_other $i18n0")
    val i18n3 = i18n0 | i18n1 | i18n2
    println(i18n3)
    println(i18n3(En)(1))
    println(i18n3(Ru)(3, i18n2, 18))*/
    println(I18n(En, One)("test"))
    println(I18n(En)("test2"))
    val ru1 = I18n(Ru)("тест") | I18n(Ru, Exact(3.0))("тест2")
    println(ru1)
  }
}