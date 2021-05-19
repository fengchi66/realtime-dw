import java.util.Properties

import com.gmall.data.common.config.{Config, KafkaConfig}
import com.gmall.data.common.source.SourceFactory
import com.gmall.data.dwd.App.GROUP_ID
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer

object KafkaConsumerTest {

  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    val kafkaConfig = KafkaConfig(Config.kafkaBrokers, GROUP_ID, "mode", "timestamp")

    val properties = new Properties()
    properties.setProperty("bootstrap.servers", kafkaConfig.brokers)
    properties.setProperty("group.id", kafkaConfig.groupId)

    val kafkaConsumer = new FlinkKafkaConsumer[String]("dwd_order_detail", new SimpleStringSchema(), properties)
    kafkaConsumer.setStartFromEarliest()

    env.addSource(kafkaConsumer).print()

    env.execute("job")


  }

}
