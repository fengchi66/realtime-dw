package com.gmall.data.dwd

import com.gmall.data.common.config.{Config, KafkaConfig, RedisConfig}
import com.gmall.data.common.entity.ods.flow.{DwdUserBaseLog, OdsUserActionLog}
import com.gmall.data.common.sink.SinkFactory
import com.gmall.data.common.source.SourceFactory
import com.gmall.data.common.utils.{Constants, GsonUtil}
import org.apache.flink.streaming.api.scala._
import com.gmall.data.dwd.Step._
import com.gmall.data.dwd.transform.EventTypeSplitFunc

object FlowTopologyV2 {

  implicit private val redisConfig = RedisConfig(Config.redisHost, Config.redisPort, Config.redisPassword, Config.redisDb)

  // 定义侧输出的标签
  private val exposeOutPut         = OutputTag[DwdUserBaseLog]("exposeOutPut")
  private val pageOutPut           = OutputTag[DwdUserBaseLog]("pageOutPut")

  // kafka输出
//  private val actionLogProducer = SinkFactory.createKafkaProducer[DwdUserBaseLog](Constants.DWD_USER_ACTION_LOG)
//  private val exposeLogProducer = SinkFactory.createKafkaProducer[DwdUserBaseLog](Constants.DWD_USER_EXPOSE_LOG)
//  private val pageLogProducer   = SinkFactory.createKafkaProducer[DwdUserBaseLog](Constants.DWD_PAGE_VIEW_LOG)

  private val actionLogProducer = SinkFactory.createProducer(Constants.DWD_USER_ACTION_LOG)
  private val exposeLogProducer = SinkFactory.createProducer(Constants.DWD_USER_EXPOSE_LOG)
  private val pageLogProducer = SinkFactory.createProducer(Constants.DWD_PAGE_VIEW_LOG)

  def build(kafkaConfig: KafkaConfig)(
    implicit env: StreamExecutionEnvironment): Unit = {

    /**
     * 1. 读取Kafka中ODS层用户行为日志
     * 2. 数据ETL处理，统一格式
     * 3. 关联eventName维度表信息
     * 4. 对点击、曝光、页面浏览事件分流
     */
    val eventStream = SourceFactory.createKafkaStream[OdsUserActionLog](kafkaConfig, Constants.ODS_USER_ACTION_LOG)
      .convertToDwdLog()
      .joinDimEvent()
      .process(EventTypeSplitFunc(exposeOutPut, pageOutPut))


    /**
     * 从侧输出的标签取出对应的曝光、页面点击测流
     */
    val exposeStream = eventStream.getSideOutput(exposeOutPut)
    val pageStream = eventStream.getSideOutput(pageOutPut)


    /**
     * 点击、曝光、页面浏览事件流输出到Kafka，作为实时数仓DWD层
     */
//    eventStream.print()
//    exposeStream.print()
//    pageStream.print()
    eventStream.map(GsonUtil.gson.toJson(_)).addSink(actionLogProducer)
    exposeStream.map(GsonUtil.gson.toJson(_)).addSink(exposeLogProducer)
    pageStream.map(GsonUtil.gson.toJson(_)).addSink(pageLogProducer)
  }

}
