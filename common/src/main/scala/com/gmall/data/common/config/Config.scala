package com.gmall.data.common.config

import com.typesafe.config.{Config, ConfigFactory}

object Config {

  private val load: Config = ConfigFactory.load()

  private val kafkaBrokers        : String = load.getString("kafka.brokers")
  private val hbaseZookeeperQuorum: String = load.getString("hbase.zookeeper.quorum")

}
