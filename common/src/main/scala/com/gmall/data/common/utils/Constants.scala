package com.gmall.data.common.utils

import java.time.format.DateTimeFormatter

/**
 * 常量类
 */
object Constants {

  val env = Config.env

  val CONSUMER_MODE_EARLIEST = "earliest"
  val CONSUMER_MODE_LATEST = "latest"
  val CONSUMER_MODE_TIMESTAMP = "timestamp"
  val CONSUMER_MODE_COMMITTED = "committed"

  val SHENCE_TRACKING_RAW_LOG_TOPIC = if ("prod".equals(env)) "shence_tracking_raw_log" else "shence_tracking_raw_log_stage"
  val SHENCE_FAILED_DECODE_RAW_LOG = "failed_decode_raw_log"
  val ODS_SC_EVENT = "ods_sc_event"
  val CMS_SC_EVENT = "cms_sc_event"

  val DT_DATE_TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
  val DATE_TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")


}
