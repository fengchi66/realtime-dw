package com.gmall.data.common.transform

import org.apache.flink.streaming.api.scala._

/**
 * sink接口，流sink函数需要继承该抽象类
 *
 * @tparam T
 */
abstract class Sink[T] {

  // 请子类取一个有意义的方法名的方法来调用sink方法
  def sink(input: DataStream[T]): Unit = {
    doSink(input)
  }

  // 基于业务,封装在doSink方法中
  protected def doSink(input: DataStream[T]): Unit

}
