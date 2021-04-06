package com.gmall.data.common.utils

import org.apache.commons.lang3.StringUtils

/**
 * StringUtil
 */
object StringUtil {

  def keyValue(srcStr: String, split1: Char, split2: Char): Map[String, String] = {
    srcStr
      .split(split1)
      .map(kv => if (kv.contains(split2)) {
        (kv.substring(0, kv.indexOf(split2)), kv.substring(kv.indexOf(split2) + 1))
      } else null)
      .filter(_ != null)
      .toMap
  }

  def paramMap(params: String): Map[String, String] = {
    if (StringUtils.isEmpty(params)) return Map()
    keyValue(params, '&', '=')
  }

  def isAnyNotEmpty(strs: String*): Boolean = {

    var count = strs.length
    for (str <- strs) {
      if (str == null || str.isEmpty)
        count -= 1
    }

    count != 0
  }

}
