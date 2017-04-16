import scala.annotation.tailrec
import scala.reflect.runtime.universe._
import ru.duester.i18n.plural.cardinal._

package ru.duester.i18n {
  package object plural {
    def getParentTypeSymbols[T](implicit tag : TypeTag[T]) : List[Symbol] = {
      @tailrec
      def getParentTypesInternal(aType : Symbol, list : List[Symbol]) : List[Symbol] = {
        if (aType == typeOf[Nothing].typeSymbol) {
          list
        }
        else {
          val parentTypeSymbol = aType.info.member(TypeName("Next")).info.typeSymbol
          getParentTypesInternal(parentTypeSymbol, list :+ aType)
        }
      }

      val aType = tag.tpe.typeSymbol
      getParentTypesInternal(aType, Nil)
    }

    //implicit def convertI18nString[]
  }
}