package com.gmall.data.common.entity.ods.gmall2021

import com.gmall.data.common.entity.ods
import com.gmall.data.common.entity.ods.OdsModel
import com.gmall.data.common.entity.ods.SqlType.SqlType
import scala.collection.mutable

// IMPORTANT!!! Don't do any changes, this is auto-generated by OdsModelGenerator
class SkuInfo extends OdsModel {
  override var database: String            = _
  override var table   : String            = _
  override var ts      : Long              = _
  override var sqlType : ods.SqlType.Value = _
  override var old     : mutable.Map[String, String] = _
  var id: String = _
var spu_id: String = _
var price: String = _
var sku_name: String = _
var sku_desc: String = _
var weight: String = _
var tm_id: String = _
var category3_id: String = _
var sku_default_img: String = _
var is_sale: String = _
var create_time: String = _

  def this(database: String, table: String, sqlType: SqlType, ts: Long, old: mutable.Map[String, String]) {
    this()
    this.database = database
    this.table = table
    this.sqlType = sqlType
    this.ts = ts
    this.old = old
  }

  override def toString = s"SkuInfo(database=$database, table=$table, sqlType=$sqlType, ts=$ts, old=$old, id=${id}, spu_id=${spu_id}, price=${price}, sku_name=${sku_name}, sku_desc=${sku_desc}, weight=${weight}, tm_id=${tm_id}, category3_id=${category3_id}, sku_default_img=${sku_default_img}, is_sale=${is_sale}, create_time=${create_time})"

}
