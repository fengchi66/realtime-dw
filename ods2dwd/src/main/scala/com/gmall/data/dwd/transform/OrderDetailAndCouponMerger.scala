package com.gmall.data.dwd.transform

import java.lang

import com.gmall.data.common.entity.dwd.DwdOrderDetail
import com.gmall.data.common.entity.ods.gmall2021.OrderDetailCoupon
import com.gmall.data.common.transform.Merger
import com.gmall.data.common.utils.LoggerUtil
import org.apache.flink.api.common.functions.CoGroupFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.EventTimeSessionWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.util.Collector
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.JavaConverters._

/**
 * 订单明细与订单coupon基于eventTimeSessionWindow实现left join
 *
 * @param input
 */
case class OrderDetailAndCouponMerger(input: DataStream[DwdOrderDetail])
  extends Merger[DwdOrderDetail, OrderDetailCoupon, DwdOrderDetail](input) {
  override def getName: String = "OrderDetailAndCouponMerger"

  override def getUid: String = "OrderDetailAndCouponMerger"

  override protected def merge(input1: DataStream[DwdOrderDetail], input2: DataStream[OrderDetailCoupon]): DataStream[DwdOrderDetail] =
    input1.coGroup(input2)
      .where(_.detail_id)
      .equalTo(_.order_detail_id)
      .window(EventTimeSessionWindows.withGap(Time.seconds(5)))
      .apply(new CoGroupFunction[DwdOrderDetail, OrderDetailCoupon, DwdOrderDetail] {
        override def coGroup(iterable: lang.Iterable[DwdOrderDetail],
                             iterable1: lang.Iterable[OrderDetailCoupon],
                             out: Collector[DwdOrderDetail]): Unit =
          try {
            val left = iterable.asScala.toSeq
            val right = iterable1.asScala.toSeq

            if (left.isEmpty)
              OrderDetailAndCouponMerger.logger.warn("DwdOrderDetail is empty")
            else {
              if (right.isEmpty) // 直接发出结果
                out.collect(left.head)
              else // 关联coupon信息
                out.collect(left.head.from(right.head))
            }
          } catch {
            case e: Exception => LoggerUtil.error(OrderDetailAndCouponMerger.logger, e,
              s"failed to OrderDetailAndCouponMerger.merger,left=${iterable},right=${iterable1}")
          }
      })
}

object OrderDetailAndCouponMerger {

  private val logger: Logger = LoggerFactory.getLogger(this.getClass)

}