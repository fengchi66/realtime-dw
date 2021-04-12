package com.gmall.data.dwd.transform

import com.gmall.data.common.entity.dwd.DwdOrderDetail
import com.gmall.data.common.entity.ods.gmall2021.{OrderDetail, OrderInfo}
import com.gmall.data.common.transform.Merger
import com.gmall.data.common.utils.LoggerUtil
import org.apache.flink.streaming.api.functions.co.ProcessJoinFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.util.Collector
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
    input1.keyBy(_.id)
      .intervalJoin(input2.keyBy(_.order_id))
      .between(Time.seconds(-5), Time.seconds(5))
      .process(new ProcessJoinFunction[OrderInfo, OrderDetail, DwdOrderDetail] {
        override def processElement(left: OrderInfo,
                                    right: OrderDetail,
                                    context: ProcessJoinFunction[OrderInfo, OrderDetail, DwdOrderDetail]#Context,
                                    out: Collector[DwdOrderDetail]): Unit =
          try {
            val orderDetail = DwdOrderDetail().from(left).from(right)
            out.collect(orderDetail)
          } catch {
            case e: Exception => LoggerUtil.error(OrderInfoAndDetailMerger.logger, e,
              s"failed to OrderInfoAndDetailMerger.merger,left=${left},right=${right}")
          }
      })
}

object OrderInfoAndDetailMerger {

  private val logger: Logger = LoggerFactory.getLogger(this.getClass)

}
