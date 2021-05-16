package com.gmall.data.common.sink

import java.util.Properties

import com.gmall.data.common.config.Config
import com.gmall.data.common.sink.kafka.EventKafkaSerializationSchema
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer.Semantic
import org.apache.kafka.clients.producer.ProducerConfig

import scala.reflect.ClassTag

/**
 * 创建一个SinkFunction
 */
object SinkFactory {

  def createKafkaProducer[T](topicName: String)(implicit classTag: ClassTag[T]): FlinkKafkaProducer[T] = {
    val props = new Properties
    props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Config.kafkaBrokers)
    props.setProperty(ProducerConfig.TRANSACTION_TIMEOUT_CONFIG, "600000") // 等待事务状态变更的最长时间10分钟
    props.setProperty(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "1") //有序
    props.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true") // 幂等性

    new FlinkKafkaProducer[T](topicName, new EventKafkaSerializationSchema[T](topicName), props, Semantic.EXACTLY_ONCE)
  }

  def createProducer(topicName: String): FlinkKafkaProducer[String] = {
    val props = new Properties
    props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Config.kafkaBrokers)

    new FlinkKafkaProducer(Config.kafkaBrokers, topicName, new SimpleStringSchema())
  }

}
