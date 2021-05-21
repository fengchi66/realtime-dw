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

    tableEnv.executeSql(
      """
        |INSERT INTO dws_core_point
        |SELECT
        | CONCAT_WS(':', 'dws_core_point', DATE_FORMAT(event_time_stamp, 'yyyyMMdd')),
        | 'uv',
        | CAST(COUNT(DISTINCT(user_id)) AS STRING)
        |FROM dwd_user_action_log
        |GROUP BY CONCAT_WS(':', 'dws_core_point', DATE_FORMAT(event_time_stamp, 'yyyyMMdd'))
        |
        |""".stripMargin)

//    tableEnv.toAppendStream[Row](table).print()


//    env.execute("job")


  }

}
