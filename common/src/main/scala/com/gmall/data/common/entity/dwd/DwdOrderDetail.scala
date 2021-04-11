package com.gmall.data.common.entity.dwd

import com.gmall.data.common.entity.ods.Model

/**
 * 订单和订单明细关联宽表对应实体类
 * "id"相关的字段，在数仓中用string类型表示
 */
class DwdOrderDetail extends Model{

  override var ts: Long = _
  // 订单明细表
  var detail_id: String = _
  var order_id: String = _
  var sku_id: String = _
  var sku_name: String = _
  var sku_num: String = _
  var order_price: Double = _
  var source_type: String = _
  // 订单表
  var province_id: String = _
  var user_id: String = _
  var order_status: String = _
  var total_amount: Double = _
  var activity_reduce_amount: Double = _
  var coupon_reduce_amount: Double = _
  var original_total_amount: Double = _
  var feight_fee: Double = _
  // 明细表
  var split_total_amount: Double = _
  var split_activity_amount: Double = _
  var split_coupon_amount: Double = _
  // 订单表
  var create_time: String = _
  var operate_time: String = _
  var expire_time: String = _
  var create_date: String = _ // 把其他字段处理得到
  var create_hour: String = _

  // 查询维表得来: 地区
  var province_name: String = _
  var province_area_code: String = _
  var province_iso_code: String = _
  var province_3166_2_code: String = _

  // 用户
  var user_age: Int = _
  var user_gender: String = _

  // 商品: 查询sku维表
  var spu_id: String = _
  var tm_id: String = _ // 品牌id
  var category3_id: String = _

  // spu
  var spu_name: String = _
  // tm
  var tm_name: String = _
  // category
  var category3_name: String = _

}
