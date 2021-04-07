import org.apache.flink.streaming.api.scala._
import org.apache.flink.table.api.EnvironmentSettings
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment

object FlinkRedisConnectorTest {

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    val settings = EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build()
    val tableEnv = StreamTableEnvironment.create(env, settings)

    tableEnv.executeSql(
      """
        |CREATE TABLE `orders` (
        |  `order_uid` STRING,
        |  `product_id` STRING,
        |  `price` DOUBLE
        |)
        |COMMENT ''
        |WITH (
        |  'connector' = 'datagen'
        |)
        |""".stripMargin)

    tableEnv.executeSql("SELECT * FROM orders").print()


  }

}
