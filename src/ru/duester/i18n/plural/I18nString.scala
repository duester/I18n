package ru.duester.i18n.plural

import scala.reflect.runtime.universe._

import ru.duester.i18n.plural.category._

// map: Language => (PluralCategory => String)
class I18nString private[plural] (private[plural] val map : Map[Symbol, Map[Symbol, String]]) {
  type Lang <: Language

  def withCategory[L >: Lang <: Language, C >: L#Category <: PluralCategory](implicit lTag : TypeTag[L], cTag : TypeTag[C]) : Option[String] = {
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
    println(s"texts: $texts")
    texts.headOption
  }

  /*def |(i18n : I18nString[_]) = ???
  def apply[L >: Lang <: Language] : String = apply[L, OTHER]
  def apply[L >: Lang <: Language, C >: L#Category <: PluralCategory] : String = {
    val oText = for {
      cMap <- map.get(lang)
      text <- cMap.get(cat)
    } yield text
    oText match {
      case Some(s) => s
      case _       => throw new Exception //
    }
  }*/
}

object I18nString {
  type Aux[L <: Language] = I18nString { type Lang = L }

  def apply[L <: Language](text : String)(implicit lTag : TypeTag[L], cTag : TypeTag[Other]) : Aux[L] = withCategory[L, Other](text)

  def withCategory[L <: Language, C >: L#Category <: PluralCategory](text : String)(implicit lTag : TypeTag[L], cTag : TypeTag[C]) : Aux[L] = {
    val map : Map[Symbol, Map[Symbol, String]] = Map(typeOf[L].typeSymbol -> Map(typeOf[C].typeSymbol -> text))
    new I18nString(map) {
      type Lang = L
    }
  }
}