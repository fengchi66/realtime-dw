package com.gmall.data.collection.flow

import com.alibaba.fastjson.{JSON, JSONArray}
import org.apache.spark.{SparkConf, SparkContext}

object SparkDemo {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("sss").setMaster("local[1]")
    val sc = new SparkContext(conf)

    sc.textFile("collections/src/main/resources/data/shence_log0509.json")
      .flatMap(r => {
        val array = JSON.parseArray(r)
        array.toArray
      })
      .saveAsTextFile("collections/src/main/resources/data/aaa")


  }

}
