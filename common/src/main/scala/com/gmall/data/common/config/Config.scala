package com.gmall.data.common.config

import com.typesafe.config.{Config, ConfigFactory}

object Config {

  private val load: Config = ConfigFactory.load()

  val kafkaBrokers        : String = load.getString("kafka.brokers")
  val hbaseZookeeperQuorum: String = load.getString("hbase.zookeeper.quorum")

}
