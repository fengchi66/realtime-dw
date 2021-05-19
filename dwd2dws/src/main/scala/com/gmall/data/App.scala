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

    tableEnv.executeSql(Sql.sales_kafka_source)

    val table = tableEnv.sqlQuery("SELECT * FROM dwd_order_detail")

    tableEnv.toAppendStream[Row](table).print()


    env.execute("job")


  }

}
