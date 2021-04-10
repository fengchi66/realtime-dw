package com.gmall.data.common.utils

import java.util.concurrent.ConcurrentHashMap

import com.gmall.data.common.config.RedisConfig
import redis.clients.jedis.exceptions.JedisConnectionException
import redis.clients.jedis.{Jedis, JedisPool, JedisPoolConfig}

import scala.collection.JavaConversions._

/**
 * Jedis的连接池
 */
object JedisConnectionPool {
  @transient private lazy val pools: ConcurrentHashMap[RedisConfig, JedisPool] =
    new ConcurrentHashMap[RedisConfig, JedisPool]()
  
  def connect(implicit re: RedisConfig): Jedis = {
    val pool = pools.getOrElseUpdate(re,
      {
        val poolConfig: JedisPoolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(250)
        poolConfig.setMaxIdle(32)
        poolConfig.setTestOnBorrow(false)
        poolConfig.setTestOnReturn(false)
        poolConfig.setTestWhileIdle(false)
        poolConfig.setMinEvictableIdleTimeMillis(60000)
        poolConfig.setTimeBetweenEvictionRunsMillis(30000)
        poolConfig.setNumTestsPerEvictionRun(-1)
        new JedisPool(poolConfig, re.host, re.port, 10000, re.password, re.db)
      }
    )
    var sleepTime: Int = 4
    var conn: Jedis = null
    while (conn == null) {
      try {
        conn = pool.getResource
      }
      catch {
        case e: JedisConnectionException if e.getCause.toString.
          contains("ERR max number of clients reached") => {
          if (sleepTime < 500) sleepTime *= 2
          Thread.sleep(sleepTime)
        }
        case e: Exception                               => throw e
      }
    }
    conn
  }
}
  

