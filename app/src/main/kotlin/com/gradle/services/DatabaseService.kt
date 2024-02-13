package com.gradle.services

import java.sql.DriverManager
import java.sql.Connection

class DatabaseService {

  var db: Connection? = null

  fun main() {
    try {
      val jdbcUrl = "postgresql://imjacob933:lQ6x2EnwISfb@ep-dawn-cloud-a526ngmf-pooler.us-east-2.aws.neon.tech/cs346-remindmed-db?sslmode=require"

      val connection = DriverManager.getConnection(jdbcUrl, "main", "JeffAvery346")

      if (!connection.isValid(0)) {
        throw Error("Failed to connect to database with jdbc")
      } else {
        db = connection
      }
    } catch (e: Error) {
      System.err.println(e)
    }
  }

  fun query(queryString: String) {
    try {
      if (db == null) {
        throw Error("Database connection is null")
      }

      val query = db.prepareStatement(queryString)

      val result = query.executeQuery()

    } catch (e: Error) {
      System.err.println(e)
    }
  }
}
