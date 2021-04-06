package com.gmall.data.common.transform

import org.apache.flink.streaming.api.scala._

/**
 * 流转换
 * @tparam T1
 * @tparam T2
 */
abstract class Convert[T1, T2] extends Serializable {

  // 请子类取一个有意义的方法名的方法来调用convert方法
  def convert(input: DataStream[T1]): DataStream[T2] = {
    doConvert(input).uid(s"convert_${getUid}").name(s"convert_${getName}")
  }

  def getUid: String

  def getName: String

  // 基于业务,封装在doConvert方法中,日志转换
  protected def doConvert(input: DataStream[T1]): DataStream[T2]

}
