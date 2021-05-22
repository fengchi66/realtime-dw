package com.gmall.data

import com.gmall.data.common.config.Config

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

  val dwd_user_action_log =
    """
      |CREATE TABLE dwd_user_action_log	(
      | event STRING COMMENT '事件',
      | event_name STRING COMMENT '时间名称',
      | user_id STRING COMMENT '用户ID',
      | distinct_id STRING COMMENT '唯一ID',
      | event_time BIGINT COMMENT '事件时间',
      | event_time_stamp TIMESTAMP(3) COMMENT '事件时间戳',
      | app_name STRING COMMENT '应用端',
      | app_version STRING COMMENT '应用的版本',
      | is_login STRING COMMENT '是否首次登陆',
      | is_vip STRING COMMENT '是否VIP',
      | wifi STRING COMMENT '是否wifi',
      | page_title STRING COMMENT '所在页面',
      | page_type STRING COMMENT '页面类型',
      | platform_type STRING COMMENT '平台类型',
      | store_id STRING COMMENT '门店ID',
      | store_name STRING COMMENT '门店名称',
      | supplier_id STRING COMMENT '供应商ID',
      | supplier_name STRING COMMENT '供应商名称',
      | room_id STRING COMMENT '直播间id',
      | room_name STRING COMMENT '直播间名称',
      | vip_level STRING COMMENT 'VIP等级',
      | lib STRING COMMENT 'SDK类型，例如python、iOS等',
      | browser STRING COMMENT '浏览器名，例如Chrome',
      | browser_version STRING COMMENT '浏览器版本，例如Chrome 45',
      | carrier STRING COMMENT '运营商名称，例如ChinaNet',
      | province STRING,
      | city STRING,
      | country STRING,
      | os STRING COMMENT '操作系统，例如iOS',
      | os_version STRING COMMENT '操作系统版本，例如8.1.1',
      | model STRING COMMENT '设备型号，例如iphone6',
      | utm_campaign STRING COMMENT '广告系列名称',
      | utm_content STRING COMMENT '广告系列内容',
      | utm_matching_type STRING COMMENT '渠道追踪匹配模式',
      | utm_medium STRING COMMENT '广告系列媒介',
      | utm_source STRING COMMENT '广告系列来源',
      | utm_term STRING COMMENT '广告系列字词',
      | url STRING COMMENT 'url',
      | referrer STRING COMMENT '向前地址',
      | scene STRING COMMENT '启动场景',
      | spu_id STRING COMMENT '商品ID',
      | spu_name STRING COMMENT '商品名',
      | spu_quantity BIGINT COMMENT '商品数量',
      | WATERMARK FOR `event_time_stamp` AS `event_time_stamp` - INTERVAL '5' SECOND
      |)
      |COMMENT '订单明细宽表'
      |WITH (
      |  'connector' = 'kafka',
      |  'format' = 'json',
      |  'properties.bootstrap.servers' = 'localhost:9092',
      |  'properties.group.id' = 'dwd-dws',
      |  'scan.startup.mode' = 'earliest-offset',
      |  'topic' = 'dwd_user_action_log'
      |)
      |""".stripMargin

  val dws_core_point =
    s"""
      |create table dws_core_point (
      |  `key` STRING,
      |  `field` STRING,
      |  `value` STRING,
      |  PRIMARY KEY (`key`) NOT ENFORCED
      |) with (
      |  'connector' = 'redis',
      |  'mode' = 'hashmap',
      |  'host' = '${Config.redisHost}',
      |  'port' = '6379',
      |  'password' = '${Config.redisPassword}',
      |  'dbNum' = '0'
      |)
      |""".stripMargin

  val dws_flow_app_pv_10min =
    """
      |CREATE TABLE dws_flow_app_pv_10min (
      |  app_name STRING,
      |  start_time STRING,
      |  end_time INT,
      |  pv BIGINT
      |) WITH (
      |   'connector' = 'jdbc',
      |   'url' = 'jdbc:mysql://localhost:3306/ads',
      |   'table-name' = 'dws_flow_app_pv_10min',
      |   'username' = 'root',
      |   'password' = '992318abC'
      |)
      |""".stripMargin

}
