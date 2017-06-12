import ru.duester.i18n.plural.cardinal._

package ru.duester.i18n {
  package object plural {
    type I18nLanguages = Root.type with En.type with Ru.type
  }
}