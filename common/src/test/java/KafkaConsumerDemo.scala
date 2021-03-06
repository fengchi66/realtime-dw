import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer

object KafkaConsumerDemo {

  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    val properties = new Properties()
    properties.setProperty("bootstrap.servers", "sc-data-03.yit.net:9092")
    properties.setProperty("group.id", "kafkaConfig.groupId")

    val kafkaConsumer = new FlinkKafkaConsumer[String]("event_topic", new SimpleStringSchema(), properties)

//    kafkaConsumer.setStartFromEarliest()

    env.addSource(kafkaConsumer).print()


    env.execute("job")

  }

}
