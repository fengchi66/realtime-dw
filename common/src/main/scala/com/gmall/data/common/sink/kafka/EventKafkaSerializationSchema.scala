package com.gmall.data.common.sink.kafka

import java.lang

import com.gmall.data.common.utils.GsonUtil
import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory

import scala.reflect.ClassTag

/**
 * 自定义Kafka序列化器，DataStream[T] -> Json[T]
 * @param topic
 * @param classTag
 * @tparam T
 */
class EventKafkaSerializationSchema[T](topic: String)(implicit classTag: ClassTag[T]) extends KafkaSerializationSchema[T]{

  // 转换为json时候保留null属性的字段
  private lazy val gson = GsonUtil.gson
  lazy val logger = LoggerFactory.getLogger(this.getClass)

  override def serialize(element: T, timestamp: lang.Long): ProducerRecord[Array[Byte], Array[Byte]] = {
    var jsonBytes: Array[Byte] = null

    try {
      jsonBytes = gson.toJson(element, classTag.runtimeClass).getBytes()
    } catch {
      case e: Exception => logger.warn(s"failed to serialize element(${element})", e)
    }

    new ProducerRecord[Array[Byte], Array[Byte]](topic, jsonBytes)
  }
}
