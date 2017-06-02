package ru.duester.i18n.plural

import scala.reflect.runtime.universe._

import ru.duester.i18n.plural.category._
import ru.duester.i18n.plural.typetag.TypeTagged
import ru.duester.i18n.plural.typetag.TypeTaggedTrait

// map: Language => (PluralCategory => String)
class I18nString[Lang <: Language : TypeTag] private[plural] (val map : Map[Symbol, Map[Symbol, String]]) extends TypeTagged[I18nString[Lang]] {
  def apply[L >: Lang <: Language](implicit lTag : TypeTag[L]) : Option[String] = apply[L, Other]

  def apply[L >: Lang <: Language, C >: L#Category <: PluralCategory](implicit lTag : TypeTag[L], cTag : TypeTag[C]) : Option[String] = {
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

  def apply[L >: Lang <: Language](parameters : Any*)(implicit lTag : TypeTag[L]) : Option[String] = apply[L, Other](parameters : _*)

  def apply[L >: Lang <: Language, C >: L#Category <: PluralCategory](parameters : Any*)(implicit lTag : TypeTag[L], cTag : TypeTag[C]) : Option[String] = {
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
  }

  def | = ???

  def languages : List[Symbol] = map.keys.toList

  def languagesAndCategories : Map[Symbol, List[Symbol]] = map.mapValues(_.keys.toList)

  override def toString : String = map.toString() // TODO
}

object I18nString {
  def apply[L <: Language](text : String)(implicit lTag : TypeTag[L]) : I18nString[L] = apply[L, Other](text)

  def apply[L <: Language, C >: L#Category <: PluralCategory](text : String)(implicit lTag : TypeTag[L], cTag : TypeTag[C]) : I18nString[L] = {
    val map : Map[Symbol, Map[Symbol, String]] = Map(typeOf[L].typeSymbol -> Map(typeOf[C].typeSymbol -> text))
    new I18nString[L](map)
  }
}