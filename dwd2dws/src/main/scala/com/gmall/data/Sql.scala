package com.gmall.data

object Sql {

  val sales_kafka_source =
    """
      |CREATE TABLE dwd_order_detail (
      |  ts BIGINT,
      |  detail_id STRING,
      |  order_id STRING,
      |  sku_id STRING,
      |  sku_name STRING,
      |  sku_num STRING,
      |  order_price STRING,
      |  source_type STRING,
      |  province_id STRING,
      |  user_id STRING,
      |  order_status STRING,
      |  total_amount STRING,
      |  activity_reduce_amount STRING,
      |  coupon_reduce_amount STRING,
      |  original_total_amount STRING,
      |  feight_fee STRING,
      |  split_total_amount STRING,
      |  split_activity_amount STRING,
      |  split_coupon_amount STRING,
      |  create_time TIMESTAMP(3),
      |  operate_time TIMESTAMP(3),
      |  expire_time TIMESTAMP(3),
      |  coupon_id STRING,
      |  province_name STRING,
      |  province_area_code STRING,
      |  province_iso_code STRING,
      |  province_3166_2_code STRING,
      |  user_birthday STRING,
      |  user_gender STRING,
      |  user_login_name STRING,
      |  spu_id STRING,
      |  tm_id STRING,
      |  category3_id STRING,
      |  spu_name STRING,
      |  tm_name STRING,
      |  category3_name STRING,
      |  dt STRING,
      |  WATERMARK FOR `create_time` AS `create_time` - INTERVAL '5' SECOND
      |)
      |COMMENT '订单明细宽表'
      |WITH (
      |  'connector' = 'kafka',
      |  'format' = 'json',
      |  'properties.bootstrap.servers' = 'localhost:9092',
      |  'properties.group.id' = 'dwd-dws',
      |  'scan.startup.mode' = 'earliest-offset',
      |  'topic' = 'dwd_order_detail'
      |)
      |""".stripMargin

}
