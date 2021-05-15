package com.gmall.data.collection.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OdsScEvent {

    private String event;
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("distinct_id")
    private String distinctId;
    private String date;
    private String time;
    @JsonProperty("app_name")
    private String appName;
    @JsonProperty("express_name")
    private String expressName;
    @JsonProperty("is_default_confirm")
    private String isDefaultConfirm;
    @JsonProperty("is_login")
    private String isLogin;
    @JsonProperty("is_vip")
    private String isVip;
    @JsonProperty("order_actual_amount")
    private String orderActualAmount;
    @JsonProperty("order_amount")
    private String orderAmount;
    @JsonProperty("order_id")
    private String orderId;
    @JsonProperty("platform_type")
    private String platformType;
    @JsonProperty("receiver_address")
    private String receiverAddress;
    @JsonProperty("receiver_area")
    private String receiverArea;
    @JsonProperty("receiver_city")
    private String receiverCity;
    @JsonProperty("receiver_name")
    private String receiverName;
    @JsonProperty("receiver_province")
    private String receiverProvince;
    @JsonProperty("transportation_costs")
    private String transportationCosts;
    @JsonProperty("vip_level")
    private String vipLevel;
    private String $lib;
    @JsonProperty("$app_version")
    private String $appVersion;
    private String $browser;
    @JsonProperty("$browser_version")
    private String $browserVersion;
    private String $carrier;
    private String $province;
    private String $city;
    private String $country;
    @JsonProperty("$is_first_day")
    private String $isFirstDay;
    private String $os;
    private String $model;
    @JsonProperty("$os_version")
    private String $osVersion;
    private String $manufacturer;
    @JsonProperty("$network_type")
    private String $networkType;
    @JsonProperty("$screen_height")
    private String $screenHeight;
    @JsonProperty("$screen_width")
    private String $screenWidth;
    private String $wifi;
    private String $ip;
    @JsonProperty("$utm_campaign")
    private String $utmCampaign;
    @JsonProperty("$utm_medium")
    private String $utmMedium;
    @JsonProperty("$utm_content")
    private String $utmContent;
    @JsonProperty("$utm_source")
    private String $utmSource;
    @JsonProperty("$utm_term")
    private String $utmTerm;
    @JsonProperty("$is_first_time")
    private String $isFirstTime;
    @JsonProperty("$url_path")
    private String $urlPath;
    private String $referrer;
    @JsonProperty("$viewport_width")
    private String $viewportWidth;
    @JsonProperty("$viewport_position")
    private String $viewportPosition;
    @JsonProperty("$viewport_height")
    private String $viewportHeight;
    @JsonProperty("$event_duration")
    private String $eventDuration;
    @JsonProperty("$sf_enter_plan_time")
    private String $sfEnterPlanTime;
    @JsonProperty("$url_query")
    private String $urlQuery;
    private String $scene;
    @JsonProperty("$share_depth")
    private String $shareDepth;
    @JsonProperty("$share_url_path")
    private String $shareUrlPath;
    @JsonProperty("$share_distinct_id")
    private String $shareDistinctId;
    @JsonProperty("$resume_from_background")
    private String $resumeFromBackground;
    @JsonProperty("$sf_audience_id")
    private String $sfAudienceId;
    @JsonProperty("$sf_channel_category")
    private String $sfChannelCategory;
    @JsonProperty("$sf_channel_service_name")
    private String $sfChannelServiceName;
    @JsonProperty("$sf_msg_id")
    private String $sfMsgId;
    @JsonProperty("$sf_plan_strategy_id")
    private String $sfPlanStrategyId;

}
