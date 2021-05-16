import com.gmall.data.common.config.{Config, RedisConfig}
import com.gmall.data.common.utils.JedisWrapper
import org.apache.flink.streaming.api.functions.sink.SinkFunction
import org.apache.flink.streaming.api.scala._

object JedisSetTest {

  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    implicit val redisConfig = RedisConfig(Config.redisHost, Config.redisPort, Config.redisPassword, Config.redisDb)

    env.readTextFile("ods2dwd/src/main/resources/event_mapping.txt")
        .addSink(new EventNameSinkFunc)

    env.execute("job")

  }

  class EventNameSinkFunc(implicit redisConfig: RedisConfig) extends SinkFunction[String] {
    override def invoke(input: String, context: SinkFunction.Context): Unit = {

      val arr = input.split("\\,", -1)
      val event = arr(0).trim
      val eventName = arr(1).trim
      JedisWrapper.wrap(jedis => {
        jedis.set("dim:event:" + event, eventName)
      }, "")
    }
  }

}
