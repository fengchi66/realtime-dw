package com.gmall.data.common.utils

import com.gmall.data.common.config.Config
import org.apache.hadoop.hbase.{HBaseConfiguration, HConstants}
import org.apache.hadoop.hbase.client.{Connection, ConnectionFactory}
import org.slf4j.{Logger, LoggerFactory}

/**
 * HBase工具类
 */
object HBaseUtil {

  private val logger: Logger = LoggerFactory.getLogger(this.getClass)

  var conn: Connection = _

  /**
   * 获取HBase连接单例对象
   * @return
   */
  def getConn(): Connection = {
    try {
      val conf = HBaseConfiguration.create()
      conf.set(HConstants.ZOOKEEPER_QUORUM, Config.hbaseZookeeperQuorum)
      conn = ConnectionFactory.createConnection(conf)
    } catch {
      case e: Exception => LoggerUtil.error(logger, e, "failed to get HBase connection")
    }
    conn
  }

}
