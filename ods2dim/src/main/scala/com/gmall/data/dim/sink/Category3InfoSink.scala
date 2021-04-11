package com.gmall.data.dim.sink

import com.gmall.data.common.entity.ods.gmall2021.BaseCategory3
import com.gmall.data.common.transform.Sink
import com.gmall.data.common.utils.{Constants, HBaseUtil, LoggerUtil}
import com.gmall.data.dim.sink.Category3InfoSink.Category3InfoSinkFunc
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.sink.{RichSinkFunction, SinkFunction}
import org.apache.flink.streaming.api.scala._
import org.apache.hadoop.hbase.TableName
import org.apache.hadoop.hbase.client.{Connection, Put, Table}
import org.apache.hadoop.hbase.util.Bytes
import org.slf4j.{Logger, LoggerFactory}

/**
 * 三级类目维度表写HBase
 *
 * @param input
 */
case class Category3InfoSink(input: DataStream[BaseCategory3])
  extends Sink[BaseCategory3] {
  override protected def doSink(input: DataStream[BaseCategory3]): Unit =
    input.addSink(new Category3InfoSinkFunc)
}

object Category3InfoSink {

  private val logger: Logger = LoggerFactory.getLogger(this.getClass)

  class Category3InfoSinkFunc extends RichSinkFunction[BaseCategory3] {

    private var conn: Connection = _
    private var table: Table = _

    override def open(parameters: Configuration): Unit = {
      conn = HBaseUtil.getConn()
      table = conn.getTable(TableName.valueOf(Constants.DIM_CATEGORY3_INFO))
    }

    override def invoke(value: BaseCategory3, context: SinkFunction.Context[_]): Unit =

      try {
        val put = new Put(Bytes.toBytes(value.id))

        put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("name"), Bytes.toBytes(value.name))
        put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("category2_id"), Bytes.toBytes(value.category2_id))

        table.put(put)
      } catch {
        case e: Exception => LoggerUtil.error(logger, e,
          s"failed to Category3InfoSinkFunc.invoke,input=${value}")
      }

    override def close(): Unit = {
      if (table != null) table.close()
      if (conn != null) conn.close()
    }
  }

}