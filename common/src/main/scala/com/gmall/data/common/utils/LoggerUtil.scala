package com.gmall.data.common.utils

import java.util.concurrent.TimeoutException

import org.slf4j.Logger

/**
 * 日志公共类
 */
object LoggerUtil {

  /**
    * 打印错误日志
    *
    * @param logger  Logger 对象
    * @param e       错误对象
    * @param message 消息体
    */
  def error(logger: Logger, e: Exception, message: String) = {
    logger.error(message + ":{},{},{}", e.getClass(), e.getMessage, e.getStackTrace)
  }

  def error(logger: Logger, e: InterruptedException, message: String) = {
    logger.error(message + ":{},{},{}", e.getClass(), e.getMessage, e.getStackTrace)
  }

  def error(logger: Logger, e: TimeoutException, message: String) = {
    logger.error(message + ":{},{},{}", e.getClass(), e.getMessage, e.getStackTrace)
  }

  def error(logger: Logger, message: String): Unit ={
    logger.error(message + ":{}", message)
  }

}
