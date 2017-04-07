package ru.duester.i18n.plural

import ru.duester.i18n.plural.category.PluralCategory
import ru.duester.i18n.plural.category.OTHER

trait I18nString {
  type Lang <: Language
  def apply[L >: Lang] : String = apply[L, OTHER]
  def apply[L >: Lang, Cat <: PluralCategory] : String
}