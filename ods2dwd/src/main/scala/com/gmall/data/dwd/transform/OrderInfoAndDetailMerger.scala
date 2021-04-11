package com.gmall.data.dwd.transform

import com.gmall.data.common.entity.dwd.DwdOrderDetail
import com.gmall.data.common.entity.ods.SqlType
import com.gmall.data.common.entity.ods.gmall2021.{OrderDetail, OrderInfo}
import com.gmall.data.common.transform.Merger
import org.apache.flink.streaming.api.scala._
import org.slf4j.{Logger, LoggerFactory}

/**
 * 订单流与订单明细流基于interval join实现双流inner join
 *
 * @param input
 */
case class OrderInfoAndDetailMerger(input: DataStream[OrderInfo])
  extends Merger[OrderInfo, OrderDetail, DwdOrderDetail](input) {
  override def getName: String = "OrderInfoAndDetailMerger"

  override def getUid: String = "OrderInfoAndDetailMerger"

  override protected def merge(input1: DataStream[OrderInfo], input2: DataStream[OrderDetail]): DataStream[DwdOrderDetail] =
    input1


}

object OrderInfoAndDetailMerger {

  private val logger: Logger = LoggerFactory.getLogger(this.getClass)

}
