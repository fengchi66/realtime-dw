package com.gmall.data.common.utils

import java.time.format.DateTimeFormatter

/**
 * 常量类
 */
object Constants {

  val CONSUMER_MODE_EARLIEST = "earliest"
  val CONSUMER_MODE_LATEST = "latest"
  val CONSUMER_MODE_TIMESTAMP = "timestamp"
  val CONSUMER_MODE_COMMITTED = "committed"

  val DT_DATE_TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
  val DATE_TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
  // mysql中的业务时间一般精确到秒级
  val DATE_TIME_MIN_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

  val KEYWORD_BLACK_LIST: Seq[String] = Seq[String]("type")
  val DB_ENV: Seq[String] = Seq[String]("prod", "stage")

  val USER_INFO_TOPIC = "gmall2021.user_info"
  val DIM_USER_INFO = "dim:dim_user_info"

  val SPU_INFO_TOPIC = "gmall2021.spu_info"
  val DIM_SPU_INFO = "dim:dim_spu_info"

  val SKU_INFO_TOPIC = "gmall2021.sku_info"
  val DIM_SKU_INFO = "dim:dim_sku_info"

  val PROVINCE_INFO_TOPIC = "gmall2021.base_province"
  val DIM_PROVINCE_INFO = "dim:dim_province_info"

  val TRADEMARK_INFO_TOPIC = "gmall2021.base_trademark"
  val DIM_TRADEMARK_INFO = "dim:dim_trademark_info"

  val CATEGORY3_INFO_TOPIC = "gmall2021.base_category3"
  val DIM_CATEGORY3_INFO = "dim:dim_category3_info"

  val ORDER_INFO_TOPIC = "gmall2021.order_info"
  val ORDER_DETAIL_TOPIC = "gmall2021.order_detail"
  val ORDER_DETAIL_COUPON_TOPIC = "gmall2021.order_detail_coupon"

  val DWD_ORDER_DETAIL_TOPIC  = "dwd_order_detail"

  val ODS_BASE_LOG_TOPIC = "ods_base_log"
  val DWD_START_LOG_TOPIC = "dwd_start_log"
  val DWD_DISPLAY_LOG_TOPIC = "dwd_display_log"
  val DWD_PAGE_LOG_TOPIC = "dwd_page_log"

  val ODS_TABLE_LIST: Seq[String] = Seq[String](
    "gmall2021.activity_info",
    "gmall2021.activity_rule",
    "gmall2021.activity_sku",
    "gmall2021.base_attr_info",
    "gmall2021.base_attr_value",
    "gmall2021.base_category1",
    "gmall2021.base_category2",
    "gmall2021.base_category3",
    "gmall2021.base_category_view",
    "gmall2021.base_dic",
    "gmall2021.base_frontend_param",
    "gmall2021.base_province",
    "gmall2021.base_region",
    "gmall2021.base_sale_attr",
    "gmall2021.base_trademark",
    "gmall2021.cart_info",
    "gmall2021.cms_banner",
    "gmall2021.comment_info",
    "gmall2021.coupon_info",
    "gmall2021.coupon_range",
    "gmall2021.coupon_use",
    "gmall2021.favor_info",
    "gmall2021.financial_sku_cost",
    "gmall2021.order_detail",
    "gmall2021.order_detail_activity",
    "gmall2021.order_detail_coupon",
    "gmall2021.order_info",
    "gmall2021.order_refund_info",
    "gmall2021.order_status_log",
    "gmall2021.payment_info",
    "gmall2021.refund_payment",
    "gmall2021.seckill_goods",
    "gmall2021.sku_attr_value",
    "gmall2021.sku_image",
    "gmall2021.sku_info",
    "gmall2021.sku_sale_attr_value",
    "gmall2021.spu_image",
    "gmall2021.spu_info",
    "gmall2021.spu_poster",
    "gmall2021.spu_sale_attr",
    "gmall2021.spu_sale_attr_value",
    "gmall2021.user_address",
    "gmall2021.user_info",
    "gmall2021.ware_info",
    "gmall2021.ware_order_task",
    "gmall2021.ware_order_task_detail",
    "gmall2021.ware_sku"
  )


}
