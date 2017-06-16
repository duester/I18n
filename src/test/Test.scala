package test

import ru.duester.i18n.plural._
import ru.duester.i18n.plural.StringInterpolators._
import ru.duester.i18n.plural.cardinal._
import ru.duester.i18n.plural.category._

object Test {
  def main(args : Array[String]) : Unit = {
    //val tpe = typeOf[Ru]
    //val m = tag.mirror
    //val cl = m.runtimeClass(tpe)

    val i18n = en"No files found."(Exact(0)) | en"Found %1$$s file."(One) | en"Found %1$$s files."(Other) | ru"Файлы не найдены."(Exact(0)) |
      ru"Найден %1$$s файл."(One) | ru"Найдено %1$$s файла."(Few) | ru"Найдено %1$$s файлов."(Other)
    println(i18n(Ru)(0, 0))
    val i18nN = enN"This is my %1$$sst time."(One) | enN"This is my %1$$snd time."(Two) | enN"This is my %1$$srd time."(Few) | enN"This is my %1$$sth time."(Other) |
      ruN"Это мой %1$$s-й раз"(Other)
    println(i18nN(ordinal.En)(21, 21))
  }
}