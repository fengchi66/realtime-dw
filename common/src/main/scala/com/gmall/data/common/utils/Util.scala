package com.gmall.data.common.utils

object Util {

  def tableClassName(table: String): String =
    table.split("_").map(_.capitalize).mkString("")

  def databasePackageName(database: String): String = database

}
