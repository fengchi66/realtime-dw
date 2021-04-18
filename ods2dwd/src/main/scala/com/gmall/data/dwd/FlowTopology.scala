package com.gmall.data.dwd

import com.gmall.data.common.config.KafkaConfig
import com.gmall.data.common.entity.ods.flow.{DwdDisplayLog, DwdPageLog, DwdStartLog, OdsBaseLog}
import com.gmall.data.common.sink.SinkFactory
import com.gmall.data.common.source.SourceFactory
import com.gmall.data.common.utils.{Constants, GsonUtil}
import org.apache.flink.streaming.api.scala._
import com.gmall.data.dwd.Step._
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer

object FlowTopology {

  private val dwdStartProducer   = SinkFactory.createKafkaProducer[DwdStartLog](Constants.DWD_START_LOG_TOPIC)
  private val dwdDisplayProducer = SinkFactory.createKafkaProducer[DwdDisplayLog](Constants.DWD_DISPLAY_LOG_TOPIC)
  private val dwdPageProducer    = SinkFactory.createKafkaProducer[DwdPageLog](Constants.DWD_PAGE_LOG_TOPIC)

  private val value1 = new FlinkKafkaProducer[String]("localhost:9092", "dwd_start_log", new SimpleStringSchema)
  private val value2 = new FlinkKafkaProducer[String]("localhost:9092", "dwd_display_log", new SimpleStringSchema)
  private val value3 = new FlinkKafkaProducer[String]("localhost:9092", "dwd_page_log", new SimpleStringSchema)


  def build(kafkaConfig: KafkaConfig)(
    implicit env: StreamExecutionEnvironment): Unit = {

    // 读取kafka中ods层的原始日志
    val odsLogStream = SourceFactory.createKafkaStream[OdsBaseLog](kafkaConfig, Constants.ODS_BASE_LOG_TOPIC)

    // ods层的数据格式转换为dwd层，并做分流
    val dwdBaseLog = odsLogStream.convert()

    // 将启动日志、曝光日志、页面访问日志分别输出到kafka
    val dwdStartLog = dwdBaseLog.filter(_.isInstanceOf[DwdStartLog])
      .map(_.asInstanceOf[DwdStartLog])

    dwdStartLog.print()
      dwdStartLog.map(GsonUtil.gson.toJson(_)).addSink(value1)

    val dwdDisplayLog = dwdBaseLog.filter(_.isInstanceOf[DwdDisplayLog])
      .map(_.asInstanceOf[DwdDisplayLog])

    dwdDisplayLog.print()
//    dwdDisplayLog.addSink(dwdDisplayProducer)
    dwdDisplayLog.map(GsonUtil.gson.toJson(_)).addSink(value2)

    val dwdPageLog = dwdBaseLog.filter(_.isInstanceOf[DwdPageLog])
      .map(_.asInstanceOf[DwdPageLog])

    dwdPageLog.print()
//    dwdPageLog.addSink(dwdPageProducer)
    dwdPageLog.map(GsonUtil.gson.toJson(_)).addSink(value3)

  }

}
