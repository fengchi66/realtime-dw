package com.gmall.data.common.entity.ods.flow

import com.gmall.data.common.utils.GsonUtil
import com.google.gson.JsonObject

/**
 * 页面访问日志
 */
class DwdPageLog extends DwdBaseLog {
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
   * 页面留存时间
   */
  var during_time: Long = _
  /**
   * home-首页, category-分类页, discovery-发现页, top_n-热门排行, favor-收藏页,
   * search-搜索页, good_list-商品列表页, good_detail-商品详情, good_spec-商品规格,
   * comment-评价, comment_done-评价完成, comment_list-评价列表, cart-购物车,
   * trade-下单结算, payment-支付页面, payment_done-支付完成, orders_all-全部订单,
   * orders_unpaid-订单待支付, orders_undelivered-订单待发货,
   * orders_unreceipted-订单待收货, orders_wait_comment-订单待评价,
   * mine-我的, activity-活动, login-登录, register-注册;
   */
  var page_id: String = _
  var last_page_id: String = _
  var item: String = _
  var item_type: String = _
  var source_type: String = _

  def from(ts: Long): DwdPageLog = {
    this.ts = ts
    this
  }

  def from(page: JsonObject): DwdPageLog = {
    this.during_time = GsonUtil.getLong(page, "during_time")
    this.page_id = GsonUtil.getString(page, "page_id")
    this.last_page_id = GsonUtil.getString(page, "last_page_id")
    this.item = GsonUtil.getString(page, "item")
    this.item_type = GsonUtil.getString(page, "item_type")
    this.source_type = GsonUtil.getString(page, "source_type")
    this
  }


  override def toString = s"DwdPageLog(ts=$ts, ar=$ar, ba=$ba, ch=$ch, is_new=$is_new, md=$md, mid=$mid, os=$os, uid=$uid, vc=$vc, during_time=$during_time, page_id=$page_id, last_page_id=$last_page_id, item=$item, item_type=$item_type, source_type=$source_type)"
}
