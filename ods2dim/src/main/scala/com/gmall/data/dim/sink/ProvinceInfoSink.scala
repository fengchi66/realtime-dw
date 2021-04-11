package com.gmall.data.dim.sink

import com.gmall.data.common.entity.ods.gmall2021.BaseProvince
import com.gmall.data.common.transform.Sink
import com.gmall.data.common.utils.{Constants, HBaseUtil, LoggerUtil}
import com.gmall.data.dim.sink.ProvinceInfoSink.ProvinceInfoSinkFunc
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.sink.{RichSinkFunction, SinkFunction}
import org.apache.flink.streaming.api.scala._
import org.apache.hadoop.hbase.TableName
import org.apache.hadoop.hbase.client.{Connection, Put, Table}
import org.apache.hadoop.hbase.util.Bytes
import org.slf4j.{Logger, LoggerFactory}

/**
 * 省份信息维度表写HBase
 * @param input
 */
case class ProvinceInfoSink(input: DataStream[BaseProvince])
  extends Sink[BaseProvince] {
  override protected def doSink(input: DataStream[BaseProvince]): Unit =
    input.addSink(new ProvinceInfoSinkFunc)
}

object ProvinceInfoSink {

  private val logger: Logger = LoggerFactory.getLogger(this.getClass)

  class ProvinceInfoSinkFunc extends RichSinkFunction[BaseProvince] {

    private var conn: Connection = _
    private var table: Table = _

    override def open(parameters: Configuration): Unit = {
      conn = HBaseUtil.getConn()
      table = conn.getTable(TableName.valueOf(Constants.DIM_PROVINCE_INFO))
    }

    override def invoke(value: BaseProvince, context: SinkFunction.Context[_]): Unit =
      try {
        val put = new Put(Bytes.toBytes(value.id))

        put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("name"), Bytes.toBytes(value.name))
        put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("region_id"), Bytes.toBytes(value.region_id))
        put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("area_code"), Bytes.toBytes(value.area_code))
        put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("iso_code"), Bytes.toBytes(value.iso_code))
        put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("iso_3166_2"), Bytes.toBytes(value.iso_3166_2))

        table.put(put)
      } catch {
        case e: Exception => LoggerUtil.error(logger, e,
          s"failed to ProvinceInfoSinkFunc.invoke,input=${value}")
      }

    override def close(): Unit = {
      if (table != null) table.close()
      if (conn != null) conn.close()
    }

  }

}