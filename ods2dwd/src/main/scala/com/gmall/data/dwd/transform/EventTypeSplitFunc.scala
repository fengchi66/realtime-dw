package com.gmall.data.dwd.transform

import com.gmall.data.common.entity.ods.flow.DwdUserBaseLog
import com.gmall.data.common.utils.Constants
import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.streaming.api.scala.OutputTag
import org.apache.flink.util.Collector

case class EventTypeSplitFunc(exposeOutPut: OutputTag[DwdUserBaseLog], pageOutPut: OutputTag[DwdUserBaseLog]) extends ProcessFunction[DwdUserBaseLog, DwdUserBaseLog] {
  override def processElement(in: DwdUserBaseLog, context: ProcessFunction[DwdUserBaseLog, DwdUserBaseLog]#Context,
                              out: Collector[DwdUserBaseLog]): Unit = {

    // 曝光埋点分流：一般会有专门的字段标识这是一个曝光埋点的事件，本demo中基于事件名来分
    if (Constants.EXPOSE_EVENT_LIST.contains(in.event)) {
      context.output(exposeOutPut, in)
    } else if (Constants.PAGE_EVENT_LIST.contains(in.event)) { // 页面浏览事件
      context.output(pageOutPut, in)
    } else {
      out.collect(in)
    }
  }
}
