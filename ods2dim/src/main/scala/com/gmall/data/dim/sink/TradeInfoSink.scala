package com.gmall.data.dim.sink

import com.gmall.data.common.entity.ods.gmall2021.BaseTrademark
import com.gmall.data.common.transform.Sink
import com.gmall.data.common.utils.{Constants, HBaseUtil, LoggerUtil}
import com.gmall.data.dim.sink.TradeInfoSink.TradeInfoSinkFunc
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.sink.{RichSinkFunction, SinkFunction}
import org.apache.flink.streaming.api.scala.DataStream
import org.apache.hadoop.hbase.TableName
import org.apache.hadoop.hbase.client.{Connection, Put, Table}
import org.apache.hadoop.hbase.util.Bytes
import org.slf4j.{Logger, LoggerFactory}

/**
 * 品牌维度表写HBase
 *
 * @param input
 */
case class TradeInfoSink(input: DataStream[BaseTrademark])
  extends Sink[BaseTrademark] {
  override protected def doSink(input: DataStream[BaseTrademark]): Unit =
    input.addSink(new TradeInfoSinkFunc)
}

object TradeInfoSink {

  private val logger: Logger = LoggerFactory.getLogger(this.getClass)

  class TradeInfoSinkFunc extends RichSinkFunction[BaseTrademark] {

    private var conn: Connection = _
    private var table: Table = _

    override def open(parameters: Configuration): Unit = {
      conn = HBaseUtil.getConn()
      table = conn.getTable(TableName.valueOf(Constants.DIM_TRADEMARK_INFO))
    }

    override def invoke(value: BaseTrademark, context: SinkFunction.Context[_]): Unit =
      try {
        val put = new Put(Bytes.toBytes(value.id))

        put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("tm_name"), Bytes.toBytes(value.tm_name))
        if (value.logo_url != null)
          put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("logo_url"), Bytes.toBytes(value.logo_url))

        table.put(put)
      } catch {
        case e: Exception => LoggerUtil.error(logger, e,
          s"failed to TradeInfoSinkFunc.invoke,input=${value}")
      }

    override def close(): Unit = {
      if (table != null) table.close()
      if (conn != null) conn.close()
    }
  }

}
