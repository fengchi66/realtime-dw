package com.gmall.data.common.entity.ods.flow

import com.gmall.data.common.utils.GsonUtil
import com.google.gson.JsonObject

/**
 * 启动日志
 */
class DwdStartLog extends DwdBaseLog {
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
  var entry: String = _ // 入口: 安装后进入=install，点击图标= icon，点击通知= notice
  var loading_time: Long = _ // 加载时长：计算下拉开始到接口返回数据的时间，（开始加载报0，加载成功或加载失败才上报时间）
  var open_ad_id: Long = _ // 开屏广告Id
  var open_ad_ms: Long = _ // 开屏广告持续时间
  var open_ad_skip_ms: Long = _ // 开屏广告点击掉过的时间，未点击为0

  def from(ts: Long): DwdStartLog = {
    this.ts = ts
    this
  }

  /**
   * 启动日志属性
   * @param start
   * @return
   */
  def from(start: JsonObject): DwdStartLog = {
    this.entry = GsonUtil.getString(start, "entry")
    this.loading_time = GsonUtil.getLong(start, "loading_time")
    this.open_ad_id = GsonUtil.getLong(start, "open_ad_id")
    this.open_ad_ms = GsonUtil.getLong(start, "open_ad_ms")
    this.open_ad_skip_ms = GsonUtil.getLong(start, "open_ad_skip_ms")
    this
  }

  override def toString = s"DwdStartLog(ts=$ts, ar=$ar, ba=$ba, ch=$ch, is_new=$is_new, md=$md, mid=$mid, os=$os, uid=$uid, vc=$vc, entry=$entry, loading_time=$loading_time, open_ad_id=$open_ad_id, open_ad_ms=$open_ad_ms, open_ad_skip_ms=$open_ad_skip_ms)"
}
