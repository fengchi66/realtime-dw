package com.gmall.data.dim

import com.gmall.data.common.config.KafkaConfig
import com.gmall.data.common.entity.ods.gmall2021.{BaseCategory3, BaseProvince, BaseTrademark, SkuInfo, SpuInfo, UserInfo}
import com.gmall.data.common.source.SourceFactory
import com.gmall.data.common.utils.Constants
import org.apache.flink.streaming.api.scala._
import com.gmall.data.dim.Step._

object StreamTopology {

  def build(kafkaConfig: KafkaConfig)(
    implicit env: StreamExecutionEnvironment): Unit = {

    // 用户
    val odsUserInfoStream = SourceFactory.createBinlogStream[UserInfo](kafkaConfig, Constants.USER_INFO_TOPIC)
    // spu
    val odsSpuInfoStream = SourceFactory.createBinlogStream[SpuInfo](kafkaConfig, Constants.SPU_INFO_TOPIC)
    // sku
    val odsSkuInfoStream = SourceFactory.createBinlogStream[SkuInfo](kafkaConfig, Constants.SKU_INFO_TOPIC)
    // 地区
    val odsProInfoStream = SourceFactory.createBinlogStream[BaseProvince](kafkaConfig, Constants.PROVINCE_INFO_TOPIC)
    // 品牌
    val odsTradeInfoStream = SourceFactory.createBinlogStream[BaseTrademark](kafkaConfig, Constants.TRADEMARK_INFO_TOPIC)
    // 三级类目
    val odsCateInfoStream = SourceFactory.createBinlogStream[BaseCategory3](kafkaConfig, Constants.CATEGORY3_INFO_TOPIC)

    /**
     * 维度数据写入HBase
     */
    odsUserInfoStream.sink(odsUserInfoStream)
    odsSpuInfoStream.sink(odsSpuInfoStream)
    odsSkuInfoStream.sink(odsSkuInfoStream)
    odsProInfoStream.sink(odsProInfoStream)
    odsTradeInfoStream.sink(odsTradeInfoStream)
    odsCateInfoStream.sink(odsCateInfoStream)


  }

}
