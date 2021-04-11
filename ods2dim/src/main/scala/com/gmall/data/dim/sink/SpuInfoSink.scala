package com.gmall.data.dim.sink

import com.gmall.data.common.entity.ods.gmall2021.SpuInfo
import com.gmall.data.common.transform.Sink
import com.gmall.data.common.utils.{Constants, HBaseUtil, LoggerUtil}
import com.gmall.data.dim.sink.SpuInfoSink.SpuInfoSinkFunc
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.sink.{RichSinkFunction, SinkFunction}
import org.apache.flink.streaming.api.scala.DataStream
import org.apache.hadoop.hbase.TableName
import org.apache.hadoop.hbase.client.{Connection, Put, Table}
import org.apache.hadoop.hbase.util.Bytes
import org.slf4j.{Logger, LoggerFactory}

/**
 * 商品:spu维度表写入HBase
 *
 * @param input
 */
case class SpuInfoSink(input: DataStream[SpuInfo])
  extends Sink[SpuInfo] {

  override protected def doSink(input: DataStream[SpuInfo]): Unit =
    input.addSink(new SpuInfoSinkFunc)
}

object SpuInfoSink {

  private val logger: Logger = LoggerFactory.getLogger(this.getClass)

  class SpuInfoSinkFunc extends RichSinkFunction[SpuInfo] {

    private var conn: Connection = _
    private var table: Table = _

    override def open(parameters: Configuration): Unit = {
      conn = HBaseUtil.getConn()
      table = conn.getTable(TableName.valueOf(Constants.DIM_SPU_INFO))
    }

    override def invoke(input: SpuInfo, context: SinkFunction.Context[_]): Unit =
      try {
        val put = new Put(Bytes.toBytes(input.id))

        put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("spu_name"), Bytes.toBytes(input.spu_name))
        put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("description"), Bytes.toBytes(input.description))
        put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("category3_id"), Bytes.toBytes(input.category3_id))
        put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("tm_id"), Bytes.toBytes(input.tm_id))

        table.put(put)
      } catch {
        case e: Exception => LoggerUtil.error(logger, e,
          s"failed to SpuInfoSinkFunc.invoke,input=${input}")
      }

    override def close(): Unit = {
      if (table != null) table.close()
      if (conn != null) conn.close()
    }
  }

}
