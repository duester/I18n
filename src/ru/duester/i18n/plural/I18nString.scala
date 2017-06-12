package ru.duester.i18n.plural

import scala.annotation.tailrec

import ru.duester.i18n.plural._
import ru.duester.i18n.plural.category._

// map: Language => (PluralCategory => String)
class I18nString[Lang <: Language] private[plural] (private val map : Map[Language, Map[PluralCategory, String]]) {
  def apply[L >: Lang <: Language](language : L)(number : Double, parameters : Any*) : Option[String] = {
    val exactCategory = language.category(number)

    val baseStringOpt = extract[L](language, exactCategory)
    baseStringOpt match {
      case None => None
      case Some(baseString) =>
        val unpackedParametersOpt = parameters.map {
          case i18 : I18nString[_] => i18.extract(language, exactCategory)
          case p @ _               => Option(p)
        }
        if (unpackedParametersOpt.contains(None)) {
          None
        }
        else {
          Some(baseString.format(unpackedParametersOpt.map { _.get } : _*))
        }
    }
  }

  private def extract[L >: Lang <: Language](language : L, exactCategory : Exact) : Option[String] = {
    val lList = languageList(language)
    val cList = categoryList(exactCategory)
    val texts = for {
      cItem <- cList
      lItem <- lList
      if map.contains(lItem)
      cMap <- map.get(lItem).toList
      if cMap.contains(cItem)
      text <- cMap.get(cItem).toList
    } yield text
    texts.headOption
  }

  def languageList(language : Language) : List[Language] = {
    @tailrec
    def languageListInternal(language : Language, list : List[Language]) : List[Language] = {
      if (language == Root) {
        list :+ Root
      }
      else {
        val parent = language.next
        languageListInternal(parent, list :+ language)
      }
    }

    languageListInternal(language, Nil)
  }

  def categoryList(exactCategory : Exact) : List[PluralCategory] = {
    @tailrec
    def categoryListInternal(category : PluralCategory, list : List[PluralCategory]) : List[PluralCategory] = {
      if (category == Other) {
        list :+ Other
      }
      else {
        val parent = category.next
        categoryListInternal(parent, list :+ category)
      }
    }

    categoryListInternal(exactCategory, Nil)
  }

  def |[OtherLang <: Language](otherI18 : I18nString[OtherLang]) : I18nString[Lang with OtherLang] = {
    @tailrec
    def buildMap(list : List[(Language, Map[PluralCategory, String])], map : Map[Language, Map[PluralCategory, String]]) : Map[Language, Map[PluralCategory, String]] = {
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

  def languages : List[Language] = map.keys.toList

  def languagesAndCategories : Map[Language, List[PluralCategory]] = map.mapValues(_.keys.toList)

  override def toString : String = {
    val langTexts = for {
      (lang, langMap) <- map.iterator
      (cat, text) <- langMap.iterator
    } yield s"${lang}[${cat}] = $text"
    langTexts mkString (" | ")
  }
}

object I18nString {
  def apply[L <: Language](language : L)(text : String) : I18nString[L] = apply(language, Other)(text)

  def apply[L <: Language, C >: L#Category <: PluralCategory](language : L, category : C)(text : String) : I18nString[L] = {
    val map : Map[Language, Map[PluralCategory, String]] = Map(language -> Map(category -> text))
    new I18nString[L](map)
  }
}