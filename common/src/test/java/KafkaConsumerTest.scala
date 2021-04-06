import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer

object KafkaConsumerTest {

  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    val properties = new Properties()
    properties.setProperty("bootstrap.servers", "106.14.115.112:9092")
    properties.setProperty("group.id", "test")

    val kafkaConsumer = new FlinkKafkaConsumer[String]("ODS_BASE_LOG", new SimpleStringSchema(), properties)
    kafkaConsumer.setStartFromEarliest()

    env.addSource(kafkaConsumer).print()

    env.execute("job")
  }

}
