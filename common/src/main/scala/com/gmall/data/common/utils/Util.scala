package com.gmall.data.common.utils

object Util {

  def tableClassName(table: String): String =
    table.split("_").map(_.capitalize).mkString("")

  def databasePackageName(database: String): String = database

  /**
   * string类型转double
   * @param item
   * @return
   */
  def toDouble(item: String): Double =
    try {
      item.toDouble
    } catch {
      case _: Throwable => 0
    }

}
