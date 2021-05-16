## 模型设计

在前面的流量数据采集中，已经将埋点上报的用户行为日志采集到ODS层，保存在Kafka中作为贴源层。在设计流量域DWD数据明细层的时候，数据建模思路是和传统离线数仓是一致的。在实时数仓分层设计中提到，与业务数据的处理略有不同，对于流量数据而言，在DWD层一般会做数据数据通过的ETL处理，如统一数据格式等以及会做维度表的关联，行为各张明细事实宽表。

### 选择业务过程

在选择业务过程之前，先简单介绍一下数据埋点，按照获取数据的类型以及用户触发行为的不同，埋点一般可以分为以下几种：

#### 点击事件

用户在应用内的每一次点击行为，都可以记为一次点击事件。比如按钮的点击，区域的点击，商品的点击，每一条新闻的点击等，都可以成为一个点击事件。

一般通过点击事件，我们可以拿到点击PV，点击UV。

#### 曝光事件

曝光事件是为了统计应用内的某些局部区域是否被用户**有效**浏览。比如推荐区域，某个按钮，首焦等等。

比如一般来说我们在衡量页面某个区域用户的点击率的时候，首先需要搞清楚的就是这个区域到底被多少用户看到了，每被用户看到一次就是一个简单的曝光事件，然后才能计算点击率。

做曝光埋点的时候需要注意两个事情：

- 第一，有效曝光的定义要科学，合理

- 第二，为了不影响页面性能以及用户体验，不能在应用内的所有区域都加曝光埋点。

#### 页面事件

页面事件通常是指页面的各种维度信息的统计，通常通过页面参数来传递。常见的比如页面浏览PV，页面浏览UV。

页面事件通常统计的信息包括以下几个部分：

- 浏览器信息：浏览器版本，浏览器语言，浏览器编码，屏幕分辨率等等；
- 访问信息：用户账号，当前页面url，上次访问时间，访问时长，页面停留时间等等；
- 来源信息：广告来源，上一页面url等等；
- 物品信息：不同的业务，这部分信息区别很大。

在了解了一些常见的数据埋点类型之后，对于流量域DWD层的建设可以分为以下3个业务事实：

- 点击
- 曝光
- 页面浏览

### 粒度

这里使用的是神策数据埋点模型，数据的粒度是event

### 维度

可以结合公司具体业务，用户行为数据一般关注的维度信息有：

- 页面
- 地理位置
- 时间
- 广告媒介
- 操作系统
- 浏览器
- 平台，如APP、小程序、H5

值的一提的是，维度数据的选择应该完全遵循DIM层一致性维度表，且维度的定义应该尽量与离线数仓保持一致。

### 事实

最常见的事实有点击、曝光、页面浏览次数等；

## DWD层数据开发

流量数据ODS -> DWD层需要做的事情：

- 通用的ETL处理

  - 空值、异常格式数据过滤等

  - 做一定的数据格式转换，通常采集到ODS的数据都是像一些类似嵌套Json的格式，可以统一格式

- 维度表的关联，基于模型设计里的维度

- 将点击、曝光、页面点击事件分流到下游不同的Kafka Topic中

> 代码详见：ods2dwd模块下 com.gmall.data.dwd.FlowTopologyV2

```scala
object FlowTopologyV2 {

  implicit private val redisConfig = RedisConfig(Config.redisHost, Config.redisPort, Config.redisPassword, Config.redisDb)

  // 定义侧输出的标签
  private val exposeOutPut         = OutputTag[DwdUserBaseLog]("exposeOutPut")
  private val pageOutPut           = OutputTag[DwdUserBaseLog]("pageOutPut")

  // kafka输出
  private val actionLogProducer = SinkFactory.createKafkaProducer[DwdUserBaseLog](Constants.DWD_USER_ACTION_LOG)
  private val exposeLogProducer = SinkFactory.createKafkaProducer[DwdUserBaseLog](Constants.DWD_USER_EXPOSE_LOG)
  private val pageLogProducer = SinkFactory.createKafkaProducer[DwdUserBaseLog](Constants.DWD_PAGE_VIEW_LOG)

  def build(kafkaConfig: KafkaConfig)(
    implicit env: StreamExecutionEnvironment): Unit = {

    /**
     * 1. 读取Kafka中ODS层用户行为日志
     * 2. 数据ETL处理，统一格式
     * 3. 关联eventName维度表信息
     * 4. 对点击、曝光、页面浏览事件分流
     */
    val eventStream = SourceFactory.createKafkaStream[OdsUserActionLog](kafkaConfig, Constants.ODS_USER_ACTION_LOG)
      .convertToDwdLog()
      .joinDimEvent()
      .process(EventTypeSplitFunc(exposeOutPut, pageOutPut))

    /**
     * 从侧输出的标签取出对应的曝光、页面点击测流
     */
    val exposeStream = eventStream.getSideOutput(exposeOutPut)
    val pageStream = eventStream.getSideOutput(pageOutPut)


    /**
     * 点击、曝光、页面浏览事件流输出到Kafka，作为实时数仓DWD层
     */
    eventStream.addSink(actionLogProducer)
    exposeStream.addSink(exposeLogProducer)
    pageStream.addSink(pageLogProducer)
  }
}
```

