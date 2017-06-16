package ru.duester.i18n.plural

import ru.duester.i18n.plural._
import ru.duester.i18n.plural.cardinal._
import ru.duester.i18n.plural.category.PluralCategory

object StringInterpolators {
  implicit class Helper(val sc : StringContext) extends AnyVal {
    def root[C <: PluralCategory : Root.type#AcceptableCategory](args : Any*)(category : C) : I18n = {
      I18n.fromStringInterpolator(sc, args : _*)(Root, category)
    }
    def rootN[C <: PluralCategory : Root.type#AcceptableCategory](args : Any*)(category : C) : I18n = {
      I18n.fromStringInterpolator(sc, args : _*)(Root, category)
    }

    def en[C <: PluralCategory : En.type#AcceptableCategory](args : Any*)(category : C) : I18n = {
      I18n.fromStringInterpolator(sc, args : _*)(En, category)
    }
    def enN[C <: PluralCategory : ordinal.En.type#AcceptableCategory](args : Any*)(category : C) : I18n = {
      I18n.fromStringInterpolator(sc, args : _*)(ordinal.En, category)
    }

    def ru[C <: PluralCategory : Ru.type#AcceptableCategory](args : Any*)(category : C) : I18n = {
      I18n.fromStringInterpolator(sc, args : _*)(Ru, category)
    }
    def ruN[C <: PluralCategory : ordinal.Ru.type#AcceptableCategory](args : Any*)(category : C) : I18n = {
      I18n.fromStringInterpolator(sc, args : _*)(ordinal.Ru, category)
    }
  }
}