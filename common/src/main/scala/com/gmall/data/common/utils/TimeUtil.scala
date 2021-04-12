package com.gmall.data.common.utils

import java.time.{Instant, LocalDateTime, ZoneId}

/**
 * 时间工具类
 */
object TimeUtil {

  /**
   * 将long类型时间戳转换为字符串格式
   * @param milliseconds
   * @return
   */
  def getDateTimeString(milliseconds: Long): String = {
    Constants.DATE_TIME_FORMATTER.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(milliseconds), ZoneId.systemDefault()))
  }

  /**
   * 将long类型时间戳转换为dt类型
   * @param milliseconds
   * @return
   */
  def getDateString(milliseconds: Long): String = {
    Constants.DT_DATE_TIME_FORMATTER.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(milliseconds), ZoneId.systemDefault()))
  }

  /**
   * 将string类型时间转换为long类型时间戳
   * @param time
   * @return
   */
  def getTimestamp(time: String): Long = try {
    LocalDateTime.from(LocalDateTime.parse(time, Constants.DATE_TIME_FORMATTER)).atZone(ZoneId.systemDefault()).toInstant.toEpochMilli
  } catch {
    case _: Throwable => 0L
  }

  /**
   * 将业务库中的秒级时间转换为dt类型
   * @param datetime
   * @return
   */
  def formatDt(datetime: String): String = {
    Constants.DT_DATE_TIME_FORMATTER.format(LocalDateTime.parse(datetime, Constants.DATE_TIME_MIN_FORMATTER))
  }


}
