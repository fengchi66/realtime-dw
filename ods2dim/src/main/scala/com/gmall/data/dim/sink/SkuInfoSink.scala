package com.gmall.data.dim.sink

import com.gmall.data.common.entity.ods.gmall2021.{SkuInfo, SpuInfo}
import com.gmall.data.common.transform.Sink
import com.gmall.data.common.utils.{Constants, HBaseUtil, LoggerUtil}
import com.gmall.data.dim.sink.SkuInfoSink.SKuInfoSinkFunc
import com.gmall.data.dim.sink.SpuInfoSink.logger
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.sink.{RichSinkFunction, SinkFunction}
import org.apache.flink.streaming.api.scala._
import org.apache.hadoop.hbase.TableName
import org.apache.hadoop.hbase.client.{Connection, Put, Table}
import org.apache.hadoop.hbase.util.Bytes
import org.slf4j.{Logger, LoggerFactory}

case class SkuInfoSink(input: DataStream[SkuInfo])
extends Sink[SkuInfo] {
  override protected def doSink(input: DataStream[SkuInfo]): Unit =
    input.addSink(new SKuInfoSinkFunc)
}

object SkuInfoSink {

  private val logger: Logger = LoggerFactory.getLogger(this.getClass)

  class SKuInfoSinkFunc extends RichSinkFunction[SkuInfo] {

    private var conn: Connection = _
    private var table: Table = _

    override def open(parameters: Configuration): Unit = {
      conn = HBaseUtil.getConn()
      table = conn.getTable(TableName.valueOf(Constants.DIM_SKU_INFO))
    }

    override def invoke(input: SkuInfo, context: SinkFunction.Context[_]): Unit =
      try {
        val put = new Put(Bytes.toBytes(input.id))

        put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("spu_id"), Bytes.toBytes(input.spu_id))
        put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("price"), Bytes.toBytes(input.price))
        put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("sku_name"), Bytes.toBytes(input.sku_name))
//        put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("sku_desc"), Bytes.toBytes(input.sku_desc))
        put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("weight"), Bytes.toBytes(input.weight))
        put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("tm_id"), Bytes.toBytes(input.tm_id))
        put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("category3_id"), Bytes.toBytes(input.category3_id))
        put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("sku_default_img"), Bytes.toBytes(input.sku_default_img))
        put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("is_sale"), Bytes.toBytes(input.is_sale))
        put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("create_time"), Bytes.toBytes(input.create_time))

        table.put(put)
      } catch {
        case e: Exception => LoggerUtil.error(logger, e,
          s"failed to SkuInfoSinkFunc.invoke,input=${input}")
      }

    override def close(): Unit = {
      if (table != null) table.close()
      if (conn != null) conn.close()
    }
  }
}
