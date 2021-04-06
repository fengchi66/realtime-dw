package com.gmall.data.common.utils

import com.google.gson.{Gson, JsonArray, JsonObject, JsonParser}


/**
 * gson公共类
 */
object GsonUtil {

  val gson = new Gson()

  val parser = new JsonParser()

  def getLong(jsonObject: JsonObject, name: String): Long = try {
    jsonObject.get(name).getAsLong
  } catch {
    case _: Throwable => 0L
  }

  def getString(jsonObject: JsonObject, name: String): String = try {
    jsonObject.get(name).getAsString
  } catch {
    case _: Throwable => null
  }

  def getAsString(jsonObject: JsonObject, name: String): String = try {
    val obj = jsonObject.get(name)
    if (obj.isJsonObject) {
      obj.getAsJsonObject.toString
    } else {
      obj.getAsString
    }
  } catch {
    case _: Throwable => null
  }

  def getJsonObject(props: JsonObject, name: String): JsonObject = try {
    props.get(name).getAsJsonObject
  } catch {
    case _: Throwable => null
  }

  def getJsonArray(column: String): JsonArray = try {
    parser.parse(column).getAsJsonArray
  } catch {
    case e: Throwable => null
  }

  def getAsJsonObject(props: JsonObject, name: String): JsonObject = try {
    val eventMore = getString(props, name)
    parser.parse(eventMore).getAsJsonObject
  } catch {
    case _: Throwable => null
  }



}
