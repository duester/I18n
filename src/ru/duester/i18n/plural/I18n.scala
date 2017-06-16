package ru.duester.i18n.plural

import scala.annotation.tailrec

import ru.duester.i18n.plural._
import ru.duester.i18n.plural.category._

// map: Language => (PluralCategory => String)
class I18n[Lang <: Language] private[plural] (private val map : Map[Language, Map[PluralCategory, String]]) {
  def apply[L >: Lang <: Language](language : L)(number : Double, parameters : Any*) : Option[String] = {
    val exactCategory = language.category(number)

    val baseStringOpt = extract[L](language, exactCategory)
    baseStringOpt match {
      case None => None
      case Some(baseString) =>
        val unpackedParametersOpt = parameters.map {
          case i18 : I18n[_] => i18.extract(language, exactCategory)
          case p @ _         => Option(p)
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
    val lList = parentList[Language](language, Root)
    val cList = parentList[PluralCategory](exactCategory, Other)
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

  def parentList[T <: { val next : T }](startItem : T, stopItem : T) : List[T] = {
    @tailrec
    def parentListInternal(item : T, list : List[T]) : List[T] = {
      if (item == stopItem) {
        list :+ stopItem
      }
      else {
        val parent = item.next
        parentListInternal(parent, list :+ item)
      }
    }

    parentListInternal(startItem, Nil)
  }

  def |[OtherLang <: Language](otherI18 : I18n[OtherLang]) : I18n[Lang with OtherLang] = {
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

    new I18n[Lang with OtherLang](buildMap(otherI18.map.iterator.toList, this.map))
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

object I18n {
  private[plural] def fromStringInterpolator[L <: Language, C <: PluralCategory : L#AcceptableCategory](sc : StringContext, args : Any*)(language : L, category : C) : I18n[L] = {
    val strings = sc.parts.toList
    val expressions = args.toList
    val totalString = strings.head + expressions.zip(strings.tail).map(p => p._1.toString + p._2).mkString
    category match {
      case Exact(number) => create(language, language.category(number).asInstanceOf[C])(totalString)
      case _             => create(language, category)(totalString)
    }
  }

  private[plural] def create[L <: Language, C <: PluralCategory](language : L, category : C)(text : String) : I18n[L] = {
    val map : Map[Language, Map[PluralCategory, String]] = Map(language -> Map(category -> text))
    new I18n[L](map)
  }
}