package com.gmall.data.common.source.binlog

import java.util

import com.alibaba.otter.canal.protocol.{FlatMessage, Message}
import com.gmall.data.common.entity.ods.SqlType
import com.gmall.data.common.utils.Util

import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.reflect.runtime.universe._

object OdsModelFactory {

  val tableClassMapping = new mutable.HashMap[String, ClassSymbol]()

  val mirror = runtimeMirror(getClass.getClassLoader)

  def build(flatMessage: FlatMessage): Seq[Any] = {

    //TODO: maybe using reflection by T class is more efficient than using reflection by loading whole classloader
    val databaseWithoutEnv = Util.databasePackageName(flatMessage.getDatabase)
    val table = flatMessage.getTable

    val classSymbol: ClassSymbol = getClassSymbol(databaseWithoutEnv, table)

    val classMirror = mirror.reflectClass(classSymbol)
    val ctor = classSymbol.typeSignature.decl(termNames.CONSTRUCTOR).alternatives.find(!_.asMethod.isPrimaryConstructor).get.asMethod
    val ctorMirror = classMirror.reflectConstructor(ctor)

    val members = classSymbol.typeSignature.members.collect {
      case m: TermSymbol if m.isVar => m
    }

    var result = Seq[Any]()

    flatMessage.getData.asScala.indices.foreach(index => {
      {
        val instance = ctorMirror(flatMessage.getDatabase, flatMessage.getTable, SqlType.withName(flatMessage.getType),
          flatMessage.getTs, getOldMap(flatMessage.getOld, index))
        val instanceMirror = mirror.reflect(instance)

        members.foreach(m => {
          val variableName = if (m.name.toString.trim.startsWith("_")) m.name.toString.trim.substring(1)
          else m.name.toString.trim

          if (flatMessage.getData.get(index).containsKey(variableName)) {
            val fieldMirror = instanceMirror.reflectField(m)
            fieldMirror.set(flatMessage.getData.get(index).get(variableName))
          }
        })

        result = result :+ instance
      }
    })

    result
  }

  private def getClassSymbol(databaseWithoutEnv: String, table: String): ClassSymbol = {
    val tableKey = s"$databaseWithoutEnv#$table"

    if (tableClassMapping.contains(tableKey) || tableClassMapping.get(tableKey) == null) {
      tableClassMapping(tableKey)
    }
    else {
      val cs = mirror.staticClass(s"com.gmall.data.common.entity.ods.$databaseWithoutEnv.${Util.tableClassName(table)}")
      tableClassMapping.put(tableKey, cs)
      cs
    }
  }

  private def getOldMap(old: util.List[util.Map[String, String]], index: Int): mutable.Map[String, String] =
    if (old != null) old.get(index).asScala else null

  // TODO: to be done
  def build(message: Message): Seq[Any] = Seq[Any]()

}
