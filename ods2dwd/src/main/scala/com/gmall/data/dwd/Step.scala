package com.gmall.data.dwd

import com.gmall.data.common.entity.dwd.DwdOrderDetail
import com.gmall.data.common.entity.ods.gmall2021.OrderInfo
import com.gmall.data.dwd.transform.{OrderDetailAndCouponMerger, OrderInfoAndDetailMerger}
import org.apache.flink.streaming.api.scala._

/**
 * 隐式转换函数抽象
 */
object Step {

  implicit def orderInfoAndDetailMerger(input: DataStream[OrderInfo]) =
    OrderInfoAndDetailMerger(input)

  implicit def orderDetailAndCouponMerger(input: DataStream[DwdOrderDetail]) =
    OrderDetailAndCouponMerger(input)

}
