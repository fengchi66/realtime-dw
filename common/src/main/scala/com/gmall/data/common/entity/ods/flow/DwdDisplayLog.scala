package com.gmall.data.common.entity.ods.flow

import com.gmall.data.common.utils.GsonUtil
import com.google.gson.{JsonElement, JsonObject}

/**
 * 曝光日志
 */
class DwdDisplayLog extends DwdBaseLog {
  override var ts: Long = _
  override var ar: String = _
  override var ba: String = _
  override var ch: String = _
  override var is_new: String = _
  override var md: String = _
  override var mid: String = _
  override var os: String = _
  override var uid: String = _
  override var vc: String = _
  /**
   * promotion-商品推广,recommend-算法推荐商品,query-查询结果商品,activity-促销活动
   */
  var display_type: String = _
  var item: String = _
  /**
   * sku_id-商品skuId,keyword-搜索关键词,sku_ids-多个商品skuId,activity_id-活动id-,coupon_id-购物券id
   */
  var item_type: String = _
  var order: Long = _
  var pos_id: String = _
  var page_id: String = _

  def from(ts: Long): DwdDisplayLog = {
    this.ts = ts
    this
  }

  /**
   * 曝光日志属性
   * @param display
   * @return
   */
  def from(display: JsonElement): DwdDisplayLog = {

    this.display_type = GsonUtil.getString(display.getAsJsonObject, "display_type")
    this.item = GsonUtil.getString(display.getAsJsonObject, "item")
    this.item_type = GsonUtil.getString(display.getAsJsonObject, "item_type")
    this.order = GsonUtil.getLong(display.getAsJsonObject, "order")
    this.pos_id = GsonUtil.getString(display.getAsJsonObject, "pos_id")
    this
  }

  /**
   * 曝光日志的页面属性
   * @param page
   * @return
   */
  def from(page: JsonObject): DwdDisplayLog = {
    this.page_id = GsonUtil.getString(page, "page_id")
    this
  }

  override def toString = s"DwdDisplayLog(ts=$ts, ar=$ar, ba=$ba, ch=$ch, is_new=$is_new, md=$md, mid=$mid, os=$os, uid=$uid, vc=$vc, display_type=$display_type, item=$item, item_type=$item_type, order=$order, pos_id=$pos_id,page_id=$page_id)"
}
