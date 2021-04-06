package com.gmall.data.common.entity.ods

import scala.collection.mutable

abstract class OdsModel extends Model with Serializable {
  var database: String
  var table   : String
  var sqlType : SqlType.Value
  var old     : mutable.Map[String, String]
  
  override def toString = s"OdsModel($database, $table, $sqlType)"
}
