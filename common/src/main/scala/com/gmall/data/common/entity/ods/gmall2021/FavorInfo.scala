package com.gmall.data.common.entity.ods.gmall2021

import com.gmall.data.common.entity.ods
import com.gmall.data.common.entity.ods.OdsModel
import com.gmall.data.common.entity.ods.SqlType.SqlType
import scala.collection.mutable

// IMPORTANT!!! Don't do any changes, this is auto-generated by OdsModelGenerator
class FavorInfo extends OdsModel {
  override var database: String            = _
  override var table   : String            = _
  override var ts      : Long              = _
  override var sqlType : ods.SqlType.Value = _
  override var old     : mutable.Map[String, String] = _
  var id: String = _
var user_id: String = _
var sku_id: String = _
var spu_id: String = _
var is_cancel: String = _
var create_time: String = _
var cancel_time: String = _

  def this(database: String, table: String, sqlType: SqlType, ts: Long, old: mutable.Map[String, String]) {
    this()
    this.database = database
    this.table = table
    this.sqlType = sqlType
    this.ts = ts
    this.old = old
  }

  override def toString = s"FavorInfo(database=$database, table=$table, sqlType=$sqlType, ts=$ts, old=$old, id=${id}, user_id=${user_id}, sku_id=${sku_id}, spu_id=${spu_id}, is_cancel=${is_cancel}, create_time=${create_time}, cancel_time=${cancel_time})"

}