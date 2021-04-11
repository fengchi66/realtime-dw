package com.gmall.data.dim

import com.gmall.data.common.entity.ods.gmall2021.{BaseCategory3, BaseProvince, BaseTrademark, SkuInfo, SpuInfo, UserInfo}
import com.gmall.data.dim.sink.{Category3InfoSink, ProvinceInfoSink, SkuInfoSink, SpuInfoSink, TradeInfoSink, UserInfoSink}
import org.apache.flink.streaming.api.scala._

object Step {

  /**
   * user_info维度表写HBase
   * @param input
   * @return
   */
  implicit def userInfoSink(input: DataStream[UserInfo]) =
    UserInfoSink(input)

  /**
   * 商品: spu维度表写入Hbase
   * @param input
   * @return
   */
  implicit def spuInfoSink(input: DataStream[SpuInfo]) =
    SpuInfoSink(input)

  /**
   * 商品: sku维度表写入HBase
   * @param input
   * @return
   */
  implicit def skuInfoSink(input: DataStream[SkuInfo]) =
    SkuInfoSink(input)

  /**
   * 省份信息维度表写Hbase
   * @param input
   * @return
   */
  implicit def provinceInfoSink(input: DataStream[BaseProvince]) =
    ProvinceInfoSink(input)

  /**
   * 品牌维度表写HBase
   * @param input
   * @return
   */
  implicit def tradeInfoSink(input: DataStream[BaseTrademark]) =
    TradeInfoSink(input)

  /**
   * 三级类目维度表写HBase
   * @param input
   * @return
   */
  implicit def category3InfoSink(input: DataStream[BaseCategory3]) =
    Category3InfoSink(input)
}
