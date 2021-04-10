package com.gmall.data.common.utils

import com.gmall.data.common.config.RedisConfig
import org.slf4j.LoggerFactory
import redis.clients.jedis.Jedis

/**
 * Redis表操作的封装
 */
object JedisWrapper {
  val logger = LoggerFactory.getLogger(this.getClass)
  
  def wrap(op: Jedis => Unit, errmsg: String)(implicit redisConfig: RedisConfig): Unit = {
    var jedisConn: Jedis = null
    try {
      jedisConn = JedisConnectionPool.connect
      op.apply(jedisConn)
    } catch {
      case e: Exception => logger.error(errmsg, e)
    } finally {
      if (jedisConn != null) {
        jedisConn.close()
      }
    }
  }
}
