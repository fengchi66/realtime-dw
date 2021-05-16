package com.gmall.data.common.entity.ods.flow

class DwdUserBaseLog {

  var event: String = _ // 事件
  var event_name: String = _ // 时间名称
  var user_id: String = _ // 用户ID
  var distinct_id: String = _ // 唯一ID
  var event_time: Long = _ // 事件时间
  var event_time_stamp: String = _ // 事件时间戳
  var app_name: String = _ // 应用端
  var app_version: String = _ // 应用的版本
  var is_login: String = _ // 是否首次登陆 1/0
  var is_vip: String = _ // 是否VIP
  var wifi: String = _
  var page_title: String = _ // 所在页面
  var page_type: String = _ // 页面类型
  var platform_type: String = _ // 平台类型
  var store_id: String = _ // 门店ID
  var store_name: String = _ // 门店名称
  var supplier_id: String = _ // 供应商ID
  var supplier_name: String = _ // 供应商名称
  var room_id: String = _ // 直播间id
  var room_name: String = _ // 直播间名称
  var vip_level: String = _ // VIP等级
  var lib: String = _ // SDK类型，例如python、iOS等
  var browser: String = _ // 浏览器名，例如Chrome
  var browser_version: String = _ // 浏览器版本，例如Chrome 45
  var carrier: String = _ // 运营商名称，例如ChinaNet
  var province: String = _
  var city: String = _
  var country: String = _
  var os: String = _ // 操作系统，例如iOS
  var os_version: String = _ // 操作系统版本，例如8.1.1
  var model: String = _ // 设备型号，例如iphone6
  var utm_campaign: String = _ // 广告系列名称
  var utm_content: String = _ // 广告系列内容
  var utm_matching_type: String = _ // 渠道追踪匹配模式
  var utm_medium: String = _ // 广告系列媒介
  var utm_source: String = _ // 广告系列来源
  var utm_term: String = _ // 广告系列字词
  var url: String = _ // url
  var referrer: String = _ // 向前地址
  var scene: String = _ // 启动场景
  var spu_id: String = _ // 商品ID
  var spu_name: String = _ // 商品名
  var spu_quantity: String = _ // 商品数量

  override def toString = s"DwdUserBaseLog(event=$event, event_name=$event_name, user_id=$user_id, distinct_id=$distinct_id, event_time=$event_time, event_time_stamp=$event_time_stamp, app_name=$app_name, app_version=$app_version, is_login=$is_login, is_vip=$is_vip, wifi=$wifi, page_title=$page_title, page_type=$page_type, platform_type=$platform_type, store_id=$store_id, store_name=$store_name, supplier_id=$supplier_id, supplier_name=$supplier_name, room_id=$room_id, room_name=$room_name, vip_level=$vip_level, lib=$lib, browser=$browser, browser_version=$browser_version, carrier=$carrier, province=$province, city=$city, country=$country, os=$os, os_version=$os_version, model=$model, utm_campaign=$utm_campaign, utm_content=$utm_content, utm_matching_type=$utm_matching_type, utm_medium=$utm_medium, utm_source=$utm_source, utm_term=$utm_term, url=$url, referrer=$referrer, scene=$scene, spu_id=$spu_id, spu_name=$spu_name, spu_quantity=$spu_quantity)"
}
