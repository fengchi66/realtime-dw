package com.gmall.data.collection.flow

import com.alibaba.fastjson.JSON
import org.apache.flink.api.common.functions.FlatMapFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector
import scala.collection.JavaConverters._

object GenLogData {

  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val stream = env.readTextFile("collections/src/main/resources/data/shence_log0509.json")


    stream.print()

    env.execute("job")

  }

}
