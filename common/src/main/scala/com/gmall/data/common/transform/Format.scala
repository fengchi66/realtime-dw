package com.gmall.data.common.transform

import org.apache.flink.streaming.api.scala._

/**
 * 流格式化
 * @tparam T
 */
abstract class Format[T] {

  // 请子类取一个有意义的方法名的方法来调用format方法
  def format(input: DataStream[T]): DataStream[T] = {
    doFormat(input).uid(s"format_${getUid}").name(s"format_${getName}")
  }

  def getUid: String

  def getName: String

  // 基于业务,封装在check方法中
  protected def doFormat(input: DataStream[T]): DataStream[T]

}
