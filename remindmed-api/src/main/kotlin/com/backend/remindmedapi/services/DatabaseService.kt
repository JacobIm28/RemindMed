package com.backend.remindmedapi.services

import jakarta.annotation.PostConstruct
import org.apache.juli.logging.Log
import org.apache.logging.log4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Import
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.sql.DriverManager
import java.sql.Connection
import java.sql.ResultSet
import java.sql.ResultSetMetaData

@Service

class DatabaseService {
  var db: Connection? = null
  @PostConstruct
  private fun main() {
    try {
      val jdbcUrl = "jdbc:postgresql://ep-dawn-cloud-a526ngmf-pooler.us-east-2.aws.neon.tech/cs346-remindmed-db?user=imjacob933&password=lQ6x2EnwISfb&sslmode=require"
      val connection = DriverManager.getConnection(jdbcUrl, "postgres", "postgres")
      if (!connection.isValid(0)) {
        throw Error("Failed to connect to database with jdbc")
      } else {
        db = connection
      }
    } catch (e: Error) {
      System.err.println(e)
    }
  }
  //create function to take query result and prepare data in an array where each array is a row of the result.
  fun prepareData(data: ResultSet): ArrayList<ArrayList<String>> {
    val result = ArrayList<ArrayList<String>>()
    val rsmd:ResultSetMetaData = data.metaData

    while (data.next()) {
      val row = ArrayList<String>()
      for (i in 1..rsmd.columnCount) {
        row.add(data.getString(i))
      }
      result.add(row)
    }
    return result
  }

  fun query(queryString: String): ArrayList<ArrayList<String>>? {
    try {
      if (db == null) {
        throw Error("Database connection is null")
      }

      val query = db!!.prepareStatement(queryString)

      val result = prepareData(query.executeQuery())

      return result

    } catch (e: Error) {
      System.err.println(e)
      return null
    }
  }
}
