import java.util.Properties

import com.alibaba.otter.canal.protocol.FlatMessage
import com.gmall.data.common.config.{Config, KafkaConfig}
import com.gmall.data.common.source.SourceFactory
import com.gmall.data.common.utils.Constants
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.slf4j.LoggerFactory

object KafkaConsumerTest {

  lazy val logger = LoggerFactory.getLogger(this.getClass)
  val OPTION_MODE      = "mode"
  val OPTION_TIMESTAMP = "timestamp"

  def main(args: Array[String]): Unit = {

    val parameterTool = ParameterTool.fromArgs(args)

    val mode = parameterTool.get(OPTION_MODE, Constants.CONSUMER_MODE_COMMITTED)
    val timestamp = parameterTool.get(OPTION_TIMESTAMP, "")

    implicit val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    val kafkaConfig = KafkaConfig(Config.kafkaBrokers, "test", mode, timestamp)

    val value = SourceFactory.createKafkaStream[FlatMessage](kafkaConfig, "gmall2021.order_info")

    value.print()

    env.execute("job")
  }

}
