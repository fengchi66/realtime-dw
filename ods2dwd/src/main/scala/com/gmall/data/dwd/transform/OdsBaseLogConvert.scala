package com.gmall.data.dwd.transform

import com.gmall.data.common.entity.ods.flow.{DwdBaseLog, DwdDisplayLog, DwdPageLog, DwdStartLog, OdsBaseLog}
import com.gmall.data.common.transform.Convert
import com.gmall.data.common.utils.{GsonUtil, LoggerUtil}
import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.JavaConverters._

/**
 * 流量数据：ods层原始日志转换为dwd层的启动、曝光、页面访问日志
 *
 * @param input
 */
case class OdsBaseLogConvert(input: DataStream[OdsBaseLog])
  extends Convert[OdsBaseLog, DwdBaseLog] {
  override def getUid: String = "OdsBaseLogConvert"

  override def getName: String = "OdsBaseLogConvert"

  def convert(): DataStream[DwdBaseLog] = super.convert(input)

  override protected def doConvert(input: DataStream[OdsBaseLog]): DataStream[DwdBaseLog] =
    input.process(new ProcessFunction[OdsBaseLog, DwdBaseLog] {
      override def processElement(input: OdsBaseLog,
                                  context: ProcessFunction[OdsBaseLog, DwdBaseLog]#Context,
                                  out: Collector[DwdBaseLog]): Unit =
        try {
          if (input.start != null) { // 启动日志
            val dwdStartLog = GsonUtil.gson.fromJson(input.common, classOf[DwdStartLog])
            out.collect(dwdStartLog.from(input.start).from(input.ts))
          } else { // 非启动日志,则为页面日志或者曝光日志(携带页面信息)
            if (input.displays != null) { //曝光日志，做flatMap
              val dwdDisplayLog = GsonUtil.gson.fromJson(input.common, classOf[DwdDisplayLog])
              for (elem <- input.displays.asScala) {
                out.collect(dwdDisplayLog.from(input.ts).from(elem).from(input.page))
              }
            } else { // 页面访问日志
              val dwdPageLog = GsonUtil.gson.fromJson(input.common, classOf[DwdPageLog])
              out.collect(dwdPageLog.from(input.page).from(input.ts))
            }
          }
        } catch {
          case e: Exception => LoggerUtil.error(OdsBaseLogConvert.logger, e,
            s"failed to OdsBaseLogConvert.doConvert,input=${input}")
        }
    })
}

object OdsBaseLogConvert {

  private val logger: Logger = LoggerFactory.getLogger(this.getClass)

}