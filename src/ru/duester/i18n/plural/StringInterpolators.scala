package ru.duester.i18n.plural

import ru.duester.i18n.plural.category.PluralCategory
import ru.duester.i18n.plural.cardinal._
import ru.duester.i18n.plural._

object StringInterpolators {
  implicit class Helper(val sc : StringContext) extends AnyVal {
    def root[C <: PluralCategory : Root.type#AcceptableCategory](args : Any*)(category : C) : I18n[Root.type] = {
      I18n.fromStringInterpolator(sc, args : _*)(Root, category)
    }
    def rootN[C <: PluralCategory : Root.type#AcceptableCategory](args : Any*)(category : C) : I18n[Root.type] = {
      I18n.fromStringInterpolator(sc, args : _*)(Root, category)
    }

    def en[C <: PluralCategory : En.type#AcceptableCategory](args : Any*)(category : C) : I18n[En.type] = {
      I18n.fromStringInterpolator(sc, args : _*)(En, category)
    }
    def enN[C <: PluralCategory : ordinal.En.type#AcceptableCategory](args : Any*)(category : C) : I18n[ordinal.En.type] = {
      I18n.fromStringInterpolator(sc, args : _*)(ordinal.En, category)
    }

    def ru[C <: PluralCategory : Ru.type#AcceptableCategory](args : Any*)(category : C) : I18n[Ru.type] = {
      I18n.fromStringInterpolator(sc, args : _*)(Ru, category)
    }
    def ruN[C <: PluralCategory : ordinal.Ru.type#AcceptableCategory](args : Any*)(category : C) : I18n[ordinal.Ru.type] = {
      I18n.fromStringInterpolator(sc, args : _*)(ordinal.Ru, category)
    }
  }
}