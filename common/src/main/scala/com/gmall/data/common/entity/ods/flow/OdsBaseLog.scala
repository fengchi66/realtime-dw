package com.gmall.data.common.entity.ods.flow

import com.google.gson.{JsonArray, JsonObject}

class OdsBaseLog {

  var actions: JsonArray = _
  var common: JsonObject = _
  var start: JsonObject = _ // 启动类型日志
  var displays: JsonArray = _ // 曝光类型日志
  var page: JsonObject = _ // 页面访问类型日志
  var ts: Long = _

  override def toString = s"OdsBaseLog(actions=$actions, common=$common, start=$start, displays=$displays, page=$page, ts=$ts)"
}
