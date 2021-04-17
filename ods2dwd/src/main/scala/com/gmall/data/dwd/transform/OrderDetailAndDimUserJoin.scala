package com.gmall.data.dwd.transform

import java.util.Objects
import java.util.concurrent.TimeUnit

import com.gmall.data.common.entity.dim.DimUserInfo
import com.gmall.data.common.entity.dwd._
import com.gmall.data.common.transform.Format
import com.gmall.data.common.utils.{Constants, HBaseUtil, LoggerUtil}
import com.gmall.data.dwd.transform.OrderDetailAndDimUserJoin.OrderDetailAndDimUserJoinFunc
import com.google.common.cache.{Cache, CacheBuilder}
import org.apache.flink.configuration.Configuration
import org.apache.flink.runtime.concurrent.Executors
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.async.{ResultFuture, RichAsyncFunction}
import org.apache.hadoop.hbase.{CellUtil, TableName}
import org.apache.hadoop.hbase.client.{AdvancedScanResultConsumer, AsyncConnection, AsyncTable, Get, Table}
import org.apache.hadoop.hbase.util.Bytes
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.{ExecutionContext, Future}

/**
 * 订单明细表与用户维度表Join
 * @param input
 */
case class OrderDetailAndDimUserJoin(input: DataStream[DwdOrderDetail])
  extends Format[DwdOrderDetail] {
  override def getUid: String = "joinDimUser"

  override def getName: String = "joinDimUser"

  def joinDimUser(): DataStream[DwdOrderDetail] = super.format(input)

  override protected def doFormat(input: DataStream[DwdOrderDetail]): DataStream[DwdOrderDetail] = {
    /**
     * 几个重要的参数说明:
     * 1.unorderedWait/orderedWait：结果的有序和无序
     * 2.Timeout： 超时参数定义了异步请求发出多久后未得到响应即被认定为失败。 它可以防止一直等待得不到响应的请求。
     * Capacity： 容量参数定义了可以同时进行的异步请求数。
     */
    AsyncDataStream.unorderedWait(input, new OrderDetailAndDimUserJoinFunc, 20, TimeUnit.SECONDS, 10)
  }
}

object OrderDetailAndDimUserJoin {

  private val logger: Logger = LoggerFactory.getLogger(this.getClass)

  /**
   * guava cache用于将数据缓存到JVM内存中,简单、强大、及轻量级。
   * 它不需要配置文件，使用起来和ConcurrentHashMap一样简单，而且能覆盖绝大多数使用cache的场景需求！
   */
  var cache: Cache[String, DimUserInfo] = _
  var asyncConn: AsyncConnection = _
  var table: AsyncTable[AdvancedScanResultConsumer] = _

  class OrderDetailAndDimUserJoinFunc() extends RichAsyncFunction[DwdOrderDetail, DwdOrderDetail] {

    // 当前线程下，用于future回调的上下文环境
    implicit lazy val executor = ExecutionContext.fromExecutor(Executors.directExecutor())

    /**
     * 1. 初始化HBaseConnection、Table等，这里使用的是HBase2.0之后的异步客户端,实现与java库的CompletableFuture
     * 2. 缓存的初始化
     * @param parameters
     */
    override def open(parameters: Configuration): Unit = {
      cache = CacheBuilder.newBuilder()
        .expireAfterWrite(2, TimeUnit.HOURS) // 设置cache中的数据在写入之后的存活时间为2小时
        .maximumSize(10000) // 设置缓存大小为10000
        .build()

      asyncConn = HBaseUtil.getAsyncConn().get()
      table = asyncConn.getTable(TableName.valueOf(Constants.DIM_USER_INFO))
    }

    override def asyncInvoke(input: DwdOrderDetail, resultFuture: ResultFuture[DwdOrderDetail]): Unit =
      try {
        // 发送异步请求，接收 future 结果
        val resultFutureRequested: Future[DwdOrderDetail] = Future {
          var dimUser = new DimUserInfo
          // 先查询缓存中数据，若缓存不存在则查询HBase
          val dimUserInfo = cache.getIfPresent(input.user_id)

          if (Objects.isNull(dimUserInfo)) { // 查询HBase
            val get = new Get(Bytes.toBytes(input.user_id)).addFamily(Bytes.toBytes("f1"))

            val result = table.get(get)
            result.get().rawCells().foreach(cell => {
              val colName = Bytes.toString(CellUtil.cloneQualifier(cell))
              val value = Bytes.toString(CellUtil.cloneValue(cell))

              colName match {
                case "birthday" => dimUser.userBirthday = value
                case "gender" => dimUser.userGender = value
                case "login_name" => dimUser.userLoginName = value
                case _ => // do nothing
              }
            })
            // 将查询结果写入到缓存中
            cache.put(input.user_id, dimUser)
          } else
            dimUser = dimUserInfo

          // 结果输出
          input.from(dimUser)
        }

        /**
         * 客户端完成请求后要执行的回调函数,将结果发给future
         */
        resultFutureRequested.onSuccess {
          case result: DwdOrderDetail => resultFuture.complete(Iterable(result))
        }
      } catch {
        case e: Exception => LoggerUtil.error(logger, e,
          s"failed to joinDimUser,input=$input")
      }

    /**
     * 当异步 I/O 请求超时的时候，默认会抛出异常并重启作业。如果你想处理超时，可以重写 AsyncFunction#timeout 方法。
     * @param input
     * @param resultFuture
     */
    override def timeout(input: DwdOrderDetail, resultFuture: ResultFuture[DwdOrderDetail]): Unit =
      super.timeout(input, resultFuture)

    /**
     * 关闭连接
     */
    override def close(): Unit = {
      if (asyncConn != null) asyncConn.close()
    }
  }

}
