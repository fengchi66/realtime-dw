package com.gmall.data.dwd.transform

import com.gmall.data.common.config.RedisConfig
import com.gmall.data.common.entity.ods.flow.DwdUserBaseLog
import com.gmall.data.common.transform.Format
import com.gmall.data.common.utils.JedisWrapper
import com.gmall.data.dwd.transform.DwdLogJoinDimEvent.DwdLogJoinDimEventFunc
import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector

case class DwdLogJoinDimEvent(input: DataStream[DwdUserBaseLog])(implicit redisConfig: RedisConfig) extends Format[DwdUserBaseLog] {
  override def getUid: String = "DwdLogJoinDimEvent"

  override def getName: String = "DwdLogJoinDimEvent"

  def joinDimEvent(): DataStream[DwdUserBaseLog] = super.format(input)

  override protected def doFormat(input: DataStream[DwdUserBaseLog]): DataStream[DwdUserBaseLog] =

    input.process(new DwdLogJoinDimEventFunc)

}

object DwdLogJoinDimEvent {

  class DwdLogJoinDimEventFunc(implicit redisConfig: RedisConfig) extends ProcessFunction[DwdUserBaseLog, DwdUserBaseLog] {
    override def processElement(i: DwdUserBaseLog,
                                context: ProcessFunction[DwdUserBaseLog, DwdUserBaseLog]#Context,
                                collector: Collector[DwdUserBaseLog]): Unit = {

      JedisWrapper.wrap(jedis => {
        // 从redis维度表中查询eventName
        val eventName = jedis.get("dim:event:" + i.event)
        i.event_name = eventName
      }, "")
      collector.collect(i)
    }
  }

}
