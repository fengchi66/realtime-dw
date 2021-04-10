package com.gmall.data.common.config

case class RedisConfig(host: String, port: Int, password: String, db: Int) extends Serializable {
}
