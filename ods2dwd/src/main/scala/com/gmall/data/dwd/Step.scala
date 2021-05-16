package com.gmall.data.dwd

import com.gmall.data.common.config.RedisConfig
import com.gmall.data.common.entity.dwd.DwdOrderDetail
import com.gmall.data.common.entity.ods.flow.{DwdUserBaseLog, OdsBaseLog, OdsUserActionLog}
import com.gmall.data.common.entity.ods.gmall2021.OrderInfo
import com.gmall.data.dwd.transform.{DwdLogJoinDimEvent, OdsActionLogConvert, OdsBaseLogConvert, OrderDetailAndCouponMerger, OrderDetailAndDimUserJoin, OrderInfoAndDetailMerger}
import org.apache.flink.streaming.api.scala._

/**
 * 隐式转换函数抽象
 */
object Step {

  implicit def orderInfoAndDetailMerger(input: DataStream[OrderInfo]) =
    OrderInfoAndDetailMerger(input)

  implicit def orderDetailAndCouponMerger(input: DataStream[DwdOrderDetail]) =
    OrderDetailAndCouponMerger(input)

  implicit def orderDetailAndDimUserJoin(input: DataStream[DwdOrderDetail]) =
    OrderDetailAndDimUserJoin(input)

  implicit def odsBaseLogConvert(input: DataStream[OdsBaseLog]) =
    OdsBaseLogConvert(input)

  implicit def odsActionLogConvert(input: DataStream[OdsUserActionLog]) =
    OdsActionLogConvert(input)

  implicit def dwdLogJoinDimEvent(input: DataStream[DwdUserBaseLog])(implicit redisConfig: RedisConfig) =
    DwdLogJoinDimEvent(input)


}
