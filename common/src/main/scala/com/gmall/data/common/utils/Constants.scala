package com.gmall.data.common.utils

import java.time.format.DateTimeFormatter

/**
 * 常量类
 */
object Constants {

  val CONSUMER_MODE_EARLIEST = "earliest"
  val CONSUMER_MODE_LATEST = "latest"
  val CONSUMER_MODE_TIMESTAMP = "timestamp"
  val CONSUMER_MODE_COMMITTED = "committed"

  val DT_DATE_TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
  val DATE_TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")

}
