package com.gmall.data

import org.apache.flink.streaming.api.scala._
import org.apache.flink.table.api.EnvironmentSettings
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment
import org.apache.flink.types.Row

object App {

  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    val bsSettings = EnvironmentSettings.newInstance.useBlinkPlanner.inStreamingMode.build
    val tableEnv = StreamTableEnvironment.create(env, bsSettings)

//    tableEnv.executeSql(Sql.sales_kafka_source)
    tableEnv.executeSql(Sql.dwd_user_action_log)
    tableEnv.executeSql(Sql.dws_core_point)
    tableEnv.execute(Sql.dws_flow_app_pv_10min)

//    tableEnv.executeSql(
//      """
//        |INSERT INTO dws_core_point
//        |SELECT
//        | CONCAT_WS(':', 'dws_core_point', DATE_FORMAT(event_time_stamp, 'yyyyMMdd')),
//        | 'uv',
//        | CAST(COUNT(DISTINCT(user_id)) AS STRING)
//        |FROM dwd_user_action_log
//        |GROUP BY CONCAT_WS(':', 'dws_core_point', DATE_FORMAT(event_time_stamp, 'yyyyMMdd'))
//        |
//        |""".stripMargin)

//    tableEnv.toAppendStream[Row](table).print()

    tableEnv.executeSql(
      """
        |INSERT INTO dws_flow_app_pv_10min
        |SELECT
        |   app_name,
        |   TUMBLE_START(event_time_stamp, INTERVAL '10' MINUTE),
        |   TUMBLE_END(event_time_stamp, INTERVAL '10' MINUTE),
        |   COUNT(1)
        |FROM dwd_user_action_log
        |GROUP BY TUMBLE(event_time_stamp, INTERVAL '10' MINUTE), app_name")
        |
        |""".stripMargin)


//    env.execute("job")


  }

}
