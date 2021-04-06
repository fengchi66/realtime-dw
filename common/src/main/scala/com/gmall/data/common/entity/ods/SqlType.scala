package com.gmall.data.common.entity.ods

/**
 * mysql binlog类型的枚举类
 * INSERT, UPDATE, DELETE对应插入,更新,删除
 */
object SqlType extends Enumeration {
  type SqlType = Value
  val INSERT, UPDATE, DELETE = Value
}
