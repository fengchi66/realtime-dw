package com.gmall.data.common.source

import java.sql.Timestamp
import java.util.Properties

import com.gmall.data.common.config.KafkaConfig
import com.gmall.data.common.utils.{Constants, GsonUtil}
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.slf4j.LoggerFactory

import scala.reflect.ClassTag

/**
 * 创建一个SourceFunction
 */
object SourceFactory extends Serializable {

  lazy val LOGGER = LoggerFactory.getLogger(this.getClass)

  def createKafkaStream[T: TypeInformation](kafkaConfig: KafkaConfig,
                                            topic: String
                                           )(implicit env: StreamExecutionEnvironment, real: ClassTag[T]): DataStream[T] = {
    val properties = new Properties()
    properties.setProperty("bootstrap.servers", kafkaConfig.brokers)
    properties.setProperty("group.id", kafkaConfig.groupId)

    val kafkaConsumer = new FlinkKafkaConsumer[String](topic, new SimpleStringSchema(), properties)

    kafkaConfig.mode match {
      case Constants.CONSUMER_MODE_EARLIEST => kafkaConsumer.setStartFromEarliest()
      case Constants.CONSUMER_MODE_LATEST => kafkaConsumer.setStartFromLatest()
      case Constants.CONSUMER_MODE_TIMESTAMP => kafkaConsumer.setStartFromTimestamp(
        Timestamp.valueOf(kafkaConfig.timestamp).getTime)
      case _ =>
    }

    def parse(item: String): T = try {
      GsonUtil.gson.fromJson(item, real.runtimeClass).asInstanceOf[T]
    } catch {
      case _: Throwable => LOGGER.warn(s"failed to parse log item, log: $item")
        null.asInstanceOf[T]
    }

    env.addSource[String](kafkaConsumer).name("source:" + topic)
      .map(r => parse(r))
  }

}
