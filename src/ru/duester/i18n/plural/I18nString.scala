package ru.duester.i18n.plural

import scala.annotation.tailrec
import scala.reflect.runtime.universe._

import ru.duester.i18n.plural._
import ru.duester.i18n.plural.category._
import ru.duester.i18n.plural.typetag._

// map: Language => (PluralCategory => String)
class I18nString[Lang <: Language : TypeTag] private[plural] (private val map : Map[Symbol, Map[Symbol, String]]) extends TypeTagged[I18nString[Lang]] {
  def apply[L >: Lang <: Language : TypeTag : PluralCategoryToTypeTag](number : Double, parameters : Any*) : Option[String] = {
    val categoryTag = implicitly[PluralCategoryToTypeTag[L]]

    val baseStringOpt = extract[L](categoryTag.category(number))
    baseStringOpt match {
      case None => None
      case Some(baseString) =>
        val unpackedParametersOpt = parameters.map {
          case t : TypeTaggedTrait[_] => t.cast[I18nString[L]].flatMap { _.extract[L](categoryTag.category(number)) }
          case p @ _                  => Option(p)
        }
        if (unpackedParametersOpt.contains(None)) {
          None
        }
        else {
          Some(baseString.format(unpackedParametersOpt.map { _.get } : _*))
        }
    }
  }

  private def extract[L >: Lang <: Language : TypeTag](categoryTag : TypeTag[_]) : Option[String] = {
    val x = implicitly[TypeTag[L]]
    val lTypeSymbols = getParentTypeSymbols(implicitly[TypeTag[L]])
    val texts = for {
      lTypeSymbol <- lTypeSymbols
      if map.contains(lTypeSymbol)
      cMap <- map.get(lTypeSymbol).toList
      cTypeSymbols = getParentTypeSymbols(categoryTag)
      cTypeSymbol <- cTypeSymbols
      if cMap.contains(cTypeSymbol)
      text <- cMap.get(cTypeSymbol).toList
    } yield text
    texts.headOption
  }

  /*def apply[L >: Lang <: Language : TypeTag] : Option[String] = apply[L, Other]

  def apply[L >: Lang <: Language : TypeTag, C >: L#Category <: PluralCategory : TypeTag] : Option[String] = {
    val lTypeSymbols = getParentTypeSymbols[L]
    val texts = for {
      lTypeSymbol <- lTypeSymbols
      if map.contains(lTypeSymbol)
      cMap <- map.get(lTypeSymbol).toList
      cTypeSymbols = getParentTypeSymbols[C]
      cTypeSymbol <- cTypeSymbols
      if cMap.contains(cTypeSymbol)
      text <- cMap.get(cTypeSymbol).toList
    } yield text
    texts.headOption
  }

  def apply[L >: Lang <: Language : TypeTag](parameters : Any*) : Option[String] = apply[L, Other](parameters : _*)

  def apply[L >: Lang <: Language : TypeTag, C >: L#Category <: PluralCategory : TypeTag](parameters : Any*) : Option[String] = {
    val baseStringOpt = apply[L, C]
    baseStringOpt match {
      case None => None
      case Some(baseString) =>
        val unpackedParametersOpt = parameters.map {
          case t : TypeTaggedTrait[_] => t.cast[I18nString[L]].flatMap { _[L, C] }
          case p @ _                  => Option(p)
        }
        if (unpackedParametersOpt.contains(None)) {
          None
        }
        else {
          Some(baseString.format(unpackedParametersOpt.map { _.get } : _*))
        }
    }
  }*/

  def getParentTypeSymbols[T](tag : TypeTag[T]) : List[Symbol] = {
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

  def |[OtherLang <: Language : TypeTag](otherI18 : I18nString[OtherLang]) : I18nString[Lang with OtherLang] = {
    @tailrec
    def buildMap(list : List[(Symbol, Map[Symbol, String])], map : Map[Symbol, Map[Symbol, String]]) : Map[Symbol, Map[Symbol, String]] = {
      if (list.isEmpty) {
        map
      }
      else {
        val (langOther, langOtherMap) = list.head
        map.get(langOther) match {
          case None          => buildMap(list.tail, map + (langOther -> langOtherMap))
          case Some(langMap) => buildMap(list.tail, map + (langOther -> (langMap ++ langOtherMap)))
        }
      }
    }

    new I18nString[Lang with OtherLang](buildMap(otherI18.map.iterator.toList, this.map))
  }

  def languages : List[Symbol] = map.keys.toList

  def languagesAndCategories : Map[Symbol, List[Symbol]] = map.mapValues(_.keys.toList)

  override def toString : String = {
    val langTexts = for {
      (langSymbol, langMap) <- map.iterator
      (catSymbol, text) <- langMap.iterator
    } yield s"${langSymbol.name}[${catSymbol.name}] = $text"
    langTexts mkString (" | ")
  }
}

object I18nString {
  def apply[L <: Language : TypeTag](text : String) : I18nString[L] = apply[L, Other](text)

  def apply[L <: Language : TypeTag, C >: L#Category <: PluralCategory : TypeTag](text : String) : I18nString[L] = {
    val map : Map[Symbol, Map[Symbol, String]] = Map(typeOf[L].typeSymbol -> Map(typeOf[C].typeSymbol -> text))
    new I18nString[L](map)
  }
}