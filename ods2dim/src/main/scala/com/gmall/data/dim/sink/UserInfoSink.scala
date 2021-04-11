package com.gmall.data.dim.sink

import com.gmall.data.common.entity.ods.gmall2021.UserInfo
import com.gmall.data.common.transform.Sink
import com.gmall.data.common.utils.{Constants, HBaseUtil, LoggerUtil}
import com.gmall.data.dim.sink.UserInfoSink.UserInfoSinkFunc
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.sink.{RichSinkFunction, SinkFunction}
import org.apache.flink.streaming.api.scala._
import org.apache.hadoop.hbase.TableName
import org.apache.hadoop.hbase.client.{Connection, Put, Table}
import org.apache.hadoop.hbase.util.Bytes
import org.slf4j.{Logger, LoggerFactory}

/**
 * 将读取到的ODS层用户维度信息写入到HBase的dim_user_info中
 *
 * @param input
 */
case class UserInfoSink(input: DataStream[UserInfo])
  extends Sink[UserInfo] {

  def sinkHBase(): Unit = super.sink(input)

  override protected def doSink(input: DataStream[UserInfo]): Unit =
    input.addSink(new UserInfoSinkFunc)
}

object UserInfoSink {

  private val logger: Logger = LoggerFactory.getLogger(this.getClass)

  class UserInfoSinkFunc extends RichSinkFunction[UserInfo] {

    private var conn: Connection = _
    private var table: Table = _

    override def open(parameters: Configuration): Unit = {
      conn = HBaseUtil.getConn()
      table = conn.getTable(TableName.valueOf(Constants.DIM_USER_INFO))
    }

    override def invoke(input: UserInfo, context: SinkFunction.Context[_]): Unit =
    try {
      val put = new Put(Bytes.toBytes(input.id))

      // 添加要上传的列
      put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("login_name"), Bytes.toBytes(input.login_name))
      put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("nick_name"), Bytes.toBytes(input.nick_name))
      //        put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("passwd"), Bytes.toBytes(input.passwd))
      put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("name"), Bytes.toBytes(input.name))
      put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("phone_num"), Bytes.toBytes(input.phone_num))
      put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("email"), Bytes.toBytes(input.email))
      //        put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("head_img"), Bytes.toBytes(input.head_img))
      put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("user_level"), Bytes.toBytes(input.user_level))
      put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("birthday"), Bytes.toBytes(input.birthday))
      put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("gender"), Bytes.toBytes(input.gender))
      put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("create_time"), Bytes.toBytes(input.create_time))
      //        put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("operate_time"), Bytes.toBytes(input.operate_time))
      //        put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("status"), Bytes.toBytes(input.status))

      table.put(put)
    } catch {
      case e: Exception => LoggerUtil.error(logger, e,
        s"failed to UserInfoSinkFunc.invoke,input=${input}")
    }

    override def close(): Unit = {
      if (table != null) table.close()
      if (conn != null) conn.close()
    }
  }

}
