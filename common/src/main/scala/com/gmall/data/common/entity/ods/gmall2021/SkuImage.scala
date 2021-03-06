package com.gmall.data.common.entity.ods.gmall2021

import com.gmall.data.common.entity.ods
import com.gmall.data.common.entity.ods.OdsModel
import com.gmall.data.common.entity.ods.SqlType.SqlType
import scala.collection.mutable

// IMPORTANT!!! Don't do any changes, this is auto-generated by OdsModelGenerator
class SkuImage extends OdsModel {
  override var database: String            = _
  override var table   : String            = _
  override var ts      : Long              = _
  override var sqlType : ods.SqlType.Value = _
  override var old     : mutable.Map[String, String] = _
  var id: String = _
var sku_id: String = _
var img_name: String = _
var img_url: String = _
var spu_img_id: String = _
var is_default: String = _

  def this(database: String, table: String, sqlType: SqlType, ts: Long, old: mutable.Map[String, String]) {
    this()
    this.database = database
    this.table = table
    this.sqlType = sqlType
    this.ts = ts
    this.old = old
  }

  override def toString = s"SkuImage(database=$database, table=$table, sqlType=$sqlType, ts=$ts, old=$old, id=${id}, sku_id=${sku_id}, img_name=${img_name}, img_url=${img_url}, spu_img_id=${spu_img_id}, is_default=${is_default})"

}
