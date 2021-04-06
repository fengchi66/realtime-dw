package com.gmall.data.tools

import java.io.InputStream
import java.sql.{Connection, DriverManager, ResultSet}

import better.files.File
import com.gmall.data.common.utils.{Constants, Util}
import org.json4s.NoTypeHints
import org.json4s.native.Serialization
import org.json4s.native.Serialization.read

import scala.collection.mutable
import scala.util.control.Breaks.{break, breakable}

object OdsModelGenerator {
  // keep empty to reset all
    val needResetTables = Constants.ODS_TABLE_LIST
  
  def main(args: Array[String]): Unit = {
    Class.forName("com.mysql.cj.jdbc.Driver")
    
    val odsMeta = OdsMeta.loadFromJson()
    val odsTemplate = loadTemplate()
    
    val sqlConnectionsMap = mutable.Map[String, Connection]()
    odsMeta.connections.foreach(connInfo => {
      breakable {
        val value = connInfo._2
        if (value == null) break
        val conn: Connection = DriverManager.getConnection(s"jdbc:mysql://${value.address}", value.username, value.password)
        sqlConnectionsMap.put(connInfo._1, conn)
      }
    })
    
    val tableMetaMap = mutable.Map[TableInfo, Seq[String]]()
    odsMeta.tables.foreach(tableInfo => {
      val conn = sqlConnectionsMap(tableInfo.connectionName)
      val rs: ResultSet = conn.getMetaData.getColumns(tableInfo.database, null, tableInfo.table, null)
      var columns = Seq[String]()
      
      while (rs.next()) {
        val columnName = rs.getString("COLUMN_NAME")
        
        columns = columns :+ {
          if (Constants.KEYWORD_BLACK_LIST.contains(columnName))
            s"_$columnName"
          else
            columnName
        }
      }
      
      tableMetaMap.put(tableInfo, columns)
    })
    
    // start to generate
    
    tableMetaMap.filter(t => needResetTables.isEmpty || needResetTables.contains(s"${t._1.database}.${t._1.table}"))
      .foreach(tableMeta => {
        
        val odsPackage = Util.databasePackageName(tableMeta._1.database)
        val odsClass = Util.tableClassName(tableMeta._1.table)
        
        val odsFields: String = tableMeta._2.map(col => s"var $col: String = _").mkString("\n")
        
        val odsFieldsToString: String = tableMeta._2.map(col => {
          s"$col=$${$col}"
        }).mkString(", ")
        
        val odsModelToString: String = s"$odsClass(database=$$database, table=$$table, sqlType=$$sqlType, ts=$$ts, " +
          s"old=$$old, ${odsFieldsToString})"
        
        val odsContent = odsTemplate.replace("{ods_model_package}", odsPackage)
          .replace("{ods_model_class_name}", odsClass)
          .replace("{ods_model_fields}", odsFields)
          .replace("{ods_model_to_string}", odsModelToString)
        
        File(s"./common/src/main/scala/com/gmall/data/common/entity/ods/${odsPackage}/${odsClass}.scala")
          .createIfNotExists(createParents = true).overwrite(odsContent)
      })
  }
  
  def loadTemplate(): String = {
    val stream: InputStream = getClass.getResourceAsStream("/ods_entity.template")
    val source = scala.io.Source.fromInputStream(stream)
    val lines = try source.mkString finally {
      source.close()
      stream.close()
    }
    
    lines
  }
  
  case class ConnectionInfo(name: String, address: String, username: String, password: String)
  
  case class TableInfo(database: String, table: String, connectionName: String)
  
  class OdsMeta {
    var connections: Map[String, ConnectionInfo] = _
    var tables     : Array[TableInfo]            = _
    
    def this(connections: Map[String, ConnectionInfo], tables: Array[TableInfo]) {
      this()
      this.connections = connections
      this.tables = tables
    }
  }
  
  object OdsMeta {
    def loadFromJson(): OdsMeta = {
      implicit val formats = Serialization.formats(NoTypeHints)
      val stream: InputStream = getClass.getResourceAsStream("/ods_meta.json")
      val source = scala.io.Source.fromInputStream(stream)
      val lines = try source.mkString finally {
        source.close()
        stream.close()
      }
      
      val odsMeta = read[OdsMeta](lines)
      odsMeta
    }
  }
  
}
