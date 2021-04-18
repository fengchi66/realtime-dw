package com.gmall.data.common.entity.ods.flow

/**
 * 流量数据dwd的基类，也就是ods层common中的字段
 */
abstract class DwdBaseLog {

  var ts: Long //事件时间
  var ar: String //区域
  var ba: String // 手机品牌
  var ch: String  // 渠道号，应用从哪个渠道来的
  var is_new: String  // 是否新用户
  var md: String  // 手机型号
  var mid: String  // 设备唯一标识
  var os: String  // 系统版本
  var uid: String  // 用户uid
  var vc: String  // 程序版本号

  override def toString = s"DwdBaseLog(ts=$ts, ar=$ar, ba=$ba, ch=$ch, is_new=$is_new, md=$md, mid=$mid, os=$os, uid=$uid, vc=$vc)"
}
