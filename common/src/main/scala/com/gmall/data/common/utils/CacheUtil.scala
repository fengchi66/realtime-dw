package com.gmall.data.common.utils

import com.google.common.cache.Cache

/**
 * 查询HBase时本地缓存
 */
object CacheUtil {

  var cache: Cache[String, String] = _


}
