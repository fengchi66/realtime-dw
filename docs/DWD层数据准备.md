#  需求分析及实现思路

## 分层需求分析

首先简单了解一下数仓的分层思路，实际上不管是离线数仓还是实时数仓，我们的分层与建模的思路是一致的。

**ODS层**

对于实时数仓来说，ods层一般是存储在kafka中的原始数据。所谓原始数据，主要有两个来源:

- 基于canal采集到kafka中的mysql表的binlog日志
- 基于flume/fluentd采集到kafka中的客户端访问日志
  - PC网页访问日志
  - H5端访问日志
  - 小程序访问日志
  - APP访问日志
- 后端网关服务日志

那么，对于ODS层的数据，我们需要做哪些处理呢？通常来说，什么都不做，ods层一般会保留数据原样，方便定位与回溯数据。

**DWD**

数据明细层，主要是对ODS层的数据做一定的清洗和主题汇总。

以用户访问日志表为例，在dwd层一般会做的事情:

- 保留和ODS层数据一样的数据维度
- 包含PC、H5、小程序、APP各个来源的数据
- 对部分枚举类型的值进行翻译
- 剔除异常数据，保证数据质量

一般在该层，还会做表之间的join、以及维表join冗余维度信息等。



在之前介绍实时数仓概念时讨论过，建设实时数仓的目的，主要是增加数据计算的复用

性，对数据以及服务之间进行解耦。每次新增加统计需求时，不至于从原始数据进行计算，而是从半成品继续加工而成。

我们这里从 kafka 的 ods 层读取用户行为日志以及业务数据，并进行简单处理，写回到kafka作为 dwd 层。

<img src="DWD层数据准备.assets/image-20210407224537117.png" alt="image-20210407224537117" style="zoom:50%;" />

## 每层的职能

| 分层 | 数据描述                                               | 数据计算          | 存储       |
| ---- | ------------------------------------------------------ | ----------------- | ---------- |
| ODS  | 原始数据，日志和业务数据                               | 日志服务器，canal | kafka      |
| DWD  | 明细数据层，比如订单、页面访问等                       | flink             | Kafka      |
|      |                                                        |                   |            |
| DIM  | 维度数据                                               | flink             | HBase      |
| DWS  | 根据某个维度主题将多个事实数据轻度聚合，形成主题宽表。 | flink             | Clickhouse |
| ADS  | 基于dws数据实时指标计算                                | flink             | myql/HBase |
| OLAP | OLAP查询                                               | clickhouse        | clickhouse |



# 业务数据DWD层

##  需求分析与思路

订单是统计分析的重要的对象，围绕订单有很多的维度统计需求，比如用户、地区、商

品、品类、品牌等等。

为了之后统计计算更加方便，减少大表之间的关联，所以在实时计算过程中将围绕订单

的相关数据整合成为一张订单的宽表。

那究竟哪些数据需要和订单整合在一起？

<img src="DWD层数据准备.assets/image-20210409233246633.png" alt="image-20210409233246633" style="zoom:50%;" />

如上图，在之前的工作中我们已经基于canal把mysql中的业务表都采集到了kafka中，每一个topic都对应一张mysql表。

下面应该做以下几方面:

- 维度表的存储
- 事实数据和事实数据关联，其实就是流与流之间的关联
-  事实数据与维度数据关联，其实就是流计算中查询外部数据源



## DIM层设计

首先应该将kafka中的维度表的信息同步到数据库中存储，维度表的设计应该考虑到维度的更新、查询数据的并发以及与业务库解耦等因素，一般会用HBase或Redis来作为实时数仓DIM层的存储。

- 对于维度变化缓慢且维度数据比较大的维度表，可以考虑用**HBase**存储，查询HBase时可以结合本地缓存 + 异步IO来实现。
- 对于维度变化较快且维表数据量相对较小的维度表，一般可以用**Redis**存储。

对于HBase维表以及Redis维表，我们考虑都实现一次。

### 读取Kafka中Canal采集的binlog数据

项目中设计了一个**`tools`**模块，该模块可以读取mysql中的表字段信息，将其映射成ODS层中Scala的类结构。执行主函数后会自动生成所有的类结构。

<img src="DWD层数据准备.assets/image-20210410000759718.png" alt="image-20210410000759718" style="zoom:50%;" />

**canal-json**

Canal 为变更日志提供了统一的格式，基于canal采集的数据为一个json格式，数据格式如下。以activity_info表为例:

```json
{
    "data":[
        {
            "id":"1",
            "activity_name":"联想专场",
            "activity_type":"3101",
            "activity_desc":"联想满减",
            "start_time":"2020-10-21 18:49:12",
            "end_time":"2020-10-31 18:49:15",
            "create_time":null
        }
    ],
    "database":"gmall2021",
    "es":1617544188000,
    "id":15,
    "isDdl":false,
    "mysqlType":{
        "id":"bigint",
        "activity_name":"varchar(200)",
        "activity_type":"varchar(10)",
        "activity_desc":"varchar(2000)",
        "start_time":"datetime",
        "end_time":"datetime",
        "create_time":"datetime"
    },
    "old":null,
    "pkNames":[
        "id"
    ],
    "sql":"",
    "sqlType":{
        "id":-5,
        "activity_name":12,
        "activity_type":12,
        "activity_desc":12,
        "start_time":93,
        "end_time":93,
        "create_time":93
    },
    "table":"activity_info",
    "ts":1617545028537,
    "type":"INSERT"
}
```

**对**canal-json**格式数据的简单说明:**

- `isDdl` ，是否是ddl变更操作，比如create table/drop table]
- `sql` ，具体的ddl sql]
- `old`，表示数据行发生更改时，对那些字段做了更改以及上一级字段的值。若   "type":"INSERT"，则old为null，在某些情况下需要关心old的值来确定当前事实。
- `ts`，数据采集时间，可作为数据的事件时间。
- `    type`， INSERT/UPDATE/DELETE

对于kafka中的canal-json的数据，这里做了一个通用的模板，用于解析flatmessage数据映射为scala类。

```scala
class {ods_model_class_name} extends OdsModel {
  override var database: String            = _
  override var table   : String            = _
  override var ts      : Long              = _
  override var sqlType : ods.SqlType.Value = _
  override var old     : mutable.Map[String, String] = _
  {ods_model_fields}

  def this(database: String, table: String, sqlType: SqlType, ts: Long, old: mutable.Map[String, String]) {
    this()
    this.database = database
    this.table = table
    this.sqlType = sqlType
    this.ts = ts
    this.old = old
  }

  override def toString = s"{ods_model_to_string}"

}
```



### 维度数据写入HBase

需求:  将**用户**、**地区**、**品牌**、**商品**、**分类**、**SPU**维度表写入HBase。

#### 用户信息维度表

- **查询mysql中用户信息表结构**

  ```sql
  mysql> desc user_info;
  +--------------+--------------+------+-----+---------+----------------+
  | Field        | Type         | Null | Key | Default | Extra          |
  +--------------+--------------+------+-----+---------+----------------+
  | id           | bigint       | NO   | PRI | NULL    | auto_increment |
  | login_name   | varchar(200) | YES  |     | NULL    |                |
  | nick_name    | varchar(200) | YES  |     | NULL    |                |
  | passwd       | varchar(200) | YES  |     | NULL    |                |
  | name         | varchar(200) | YES  |     | NULL    |                |
  | phone_num    | varchar(200) | YES  |     | NULL    |                |
  | email        | varchar(200) | YES  |     | NULL    |                |
  | head_img     | varchar(200) | YES  |     | NULL    |                |
  | user_level   | varchar(200) | YES  |     | NULL    |                |
  | birthday     | date         | YES  |     | NULL    |                |
  | gender       | varchar(1)   | YES  |     | NULL    |                |
  | create_time  | datetime     | YES  |     | NULL    |                |
  | operate_time | datetime     | YES  |     | NULL    |                |
  | status       | varchar(200) | YES  |     | NULL    |                |
  +--------------+--------------+------+-----+---------+----------------+
  14 rows in set (0.01 sec)
  ```

  - 该用户信息表中包含了用户的一些基本信息，如用户名、密码、性别等信息，在制作用户维度表的时候，可以将这些字段全部存到HBase。事实上，某一张维度表的信息的来源往往可能是多张ODS表，这个时候，选用HBase作为实时数仓的DIM层存储介质的优势就体现出来了，它的列可以动态扩容，甚至可以基于rowkey设置多个列簇，将具有相同规律的列存放在同一个列簇下。开源Flink Sql也支持以时态表的方式读取HBase，选用HBase也是为Flink实时计算平台化做准备。

- **HBase创建表**

  ```shell
  create table 'dim:dim_user_info',NAME=>'f1'
  ```

  - 用户维度表以user_id作为rowkey，其余13列用户信息存在列簇f1下。
  - 在使用HBase的时候，一定要注意表的rowkey以及预分区的设计，否则可能带来严重的数据热点问题，影响线上数据服务。











