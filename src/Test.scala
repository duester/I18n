import scala.annotation.tailrec
import scala.reflect.runtime.universe._

import ru.duester.i18n.plural._
import ru.duester.i18n.plural.cardinal._
import ru.duester.i18n.plural.category._

object Test {
  def main(args : Array[String]) : Unit = {
    val i18n = I18nString[Ru]("ru_other")
    val i18n2 = I18nString.withCategory[Ru, Few]("ru_few")
    val tpe = typeOf[Ru]
    //val m = tag.mirror
    //val cl = m.runtimeClass(tpe)
    println(getParentTypeSymbols(tpe.typeSymbol))
  }

  def getParentTypeSymbols(aType : Symbol) : List[Symbol] = {
    @tailrec
    def getParentTypesInternal(aType : Symbol, list : List[Symbol]) : List[Symbol] = {
      if (aType == typeOf[Nothing].typeSymbol) {
        list
      }
      else {
        val parentTypeSymbol = aType.info.member(TypeName("Parent")).asType.info.typeSymbol
        getParentTypesInternal(parentTypeSymbol, list :+ aType)
      }
    }

    getParentTypesInternal(aType, Nil)
  }
}