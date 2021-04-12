package com.gmall.data.common.entity.dwd

import java.util.Objects

import com.gmall.data.common.entity.ods.Model
import com.gmall.data.common.entity.ods.gmall2021.{OrderDetail, OrderInfo}
import com.gmall.data.common.utils.{TimeUtil, Util}

/**
 * 订单和订单明细关联宽表对应实体类
 * "id"相关的字段，在数仓中用string类型表示
 */
class DwdOrderDetail extends Model with Serializable {

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
  // 事实的分区字段，由下单时间格式化
  var dt: String = _

  /**
   * 从订单表中取的字段
   * @param r
   * @return
   */
  def from(r: OrderInfo): DwdOrderDetail = {
    if (Objects.nonNull(r)) {
      this.order_id = r.id
      this.province_id = r.province_id
      this.user_id = r.user_id
      this.order_status = r.order_status
      this.total_amount = Util.toDouble(r.total_amount)
      this.activity_reduce_amount = Util.toDouble(r.activity_reduce_amount)
      this.coupon_reduce_amount = Util.toDouble(r.coupon_reduce_amount)
      this.original_total_amount = Util.toDouble(r.original_total_amount)
      this.feight_fee = Util.toDouble(r.feight_fee)
      this.create_time = r.create_time
      this.operate_time = r.operate_time
      this.expire_time = r.expire_time
      this.ts = r.ts
      this.dt = TimeUtil.formatDt(r.create_time)
    }
    this
  }

  /**
   * 从订单明细表中取的字段
   * @param r
   * @return
   */
  def from(r: OrderDetail): DwdOrderDetail = {
    if (Objects.nonNull(r)) {
      this.detail_id = r.id
      this.sku_id = r.sku_id
      this.sku_name = r.sku_name
      this.sku_num = r.sku_num
      this.order_price = Util.toDouble(r.order_price)
      this.source_type = r.source_type
      this.split_total_amount = Util.toDouble(r.split_total_amount)
      this.split_activity_amount = Util.toDouble(r.split_activity_amount)
      this.split_coupon_amount = Util.toDouble(r.split_coupon_amount)
    }
    this
  }

  override def toString = s"DwdOrderDetail(ts=$ts, detail_id=$detail_id, order_id=$order_id, sku_id=$sku_id, sku_name=$sku_name, sku_num=$sku_num, order_price=$order_price, source_type=$source_type, province_id=$province_id, user_id=$user_id, order_status=$order_status, total_amount=$total_amount, activity_reduce_amount=$activity_reduce_amount, coupon_reduce_amount=$coupon_reduce_amount, original_total_amount=$original_total_amount, feight_fee=$feight_fee, split_total_amount=$split_total_amount, split_activity_amount=$split_activity_amount, split_coupon_amount=$split_coupon_amount, create_time=$create_time, operate_time=$operate_time, expire_time=$expire_time, province_name=$province_name, province_area_code=$province_area_code, province_iso_code=$province_iso_code, province_3166_2_code=$province_3166_2_code, user_age=$user_age, user_gender=$user_gender, spu_id=$spu_id, tm_id=$tm_id, category3_id=$category3_id, spu_name=$spu_name, tm_name=$tm_name, category3_name=$category3_name, dt=$dt)"
}

object DwdOrderDetail {
  def apply(): DwdOrderDetail = new DwdOrderDetail()
}
