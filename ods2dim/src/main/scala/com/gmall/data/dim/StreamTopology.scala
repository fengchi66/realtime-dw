package com.gmall.data.dim

import com.gmall.data.common.config.KafkaConfig
import com.gmall.data.common.entity.ods.gmall2021.UserInfo
import com.gmall.data.common.source.SourceFactory
import com.gmall.data.common.utils.Constants
import org.apache.flink.streaming.api.scala._

object StreamTopology {

  def build(kafkaConfig: KafkaConfig)(
    implicit env: StreamExecutionEnvironment): Unit = {

    val odsUserInfoStream = SourceFactory.createBinlogStream[UserInfo](kafkaConfig, Constants.USER_INFO_TOPIC)

    odsUserInfoStream.print()


  }

}
