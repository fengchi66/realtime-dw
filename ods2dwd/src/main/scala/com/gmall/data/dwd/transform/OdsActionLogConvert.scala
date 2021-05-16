package com.gmall.data.dwd.transform

import com.gmall.data.common.entity.ods.flow.{DwdUserBaseLog, OdsUserActionLog}
import com.gmall.data.common.transform.Convert
import com.gmall.data.common.utils.TimeUtil
import org.apache.flink.streaming.api.scala._

/**
 * ods层的用户行为日志转换为dwd
 * @param input
 */
case class OdsActionLogConvert(input: DataStream[OdsUserActionLog]) extends Convert[OdsUserActionLog, DwdUserBaseLog] {
  override def getUid: String = "OdsActionLogConvert"

  override def getName: String = "OdsActionLogConvert"

  def convertToDwdLog(): DataStream[DwdUserBaseLog] = super.convert(input)

  override protected def doConvert(input: DataStream[OdsUserActionLog]): DataStream[DwdUserBaseLog] =
    input.map(r => {

      val log = new DwdUserBaseLog

      log.event = r.event
      log.user_id = r.user_id
      log.distinct_id = r.distinct_id
      log.event_time = TimeUtil.getTimestamp(r.time)
      log.event_time_stamp = r.time
      log.app_name = r.app_name
      log.app_version = r.$app_version
      log.is_login = r.is_login
      log.is_vip = r.is_vip
      log.wifi = r.$wifi
      log.page_title = r.page_title
      log.page_type = r.page_type
      log.platform_type = r.platform_type
      log.url = r.$url
      log.referrer = r.$referrer
      log.vip_level = r.vip_level
      log.lib = r.$lib
      log.browser = r.$browser
      log.browser_version = r.$browser_version
      log.carrier = r.$carrier
      log.province = r.$province
      log.city = r.$city
      log.country = r.$country
      log.os = r.$os
      log.os_version = r.$os_version
      log.model = r.$model
      log.utm_campaign = r.$utm_campaign
      log.utm_content = r.$utm_content
      log.utm_medium = r.$utm_medium
      log.utm_source = r.$utm_source
      log.utm_term = r.$utm_term
      log.scene = r.$scene
      log.spu_id = r.commodity_id
      log.spu_name = r.commodity_name
      log.spu_quantity = r.commodity_quantity
      log.store_id = r.store_id
      log.store_name = r.store_name
      log.supplier_id = r.supplier_id
      log.supplier_name = r.supplier_name
      log.room_id = r.room_id
      log.room_name = r.room_name

      log
    })
}
