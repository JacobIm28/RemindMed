package com.gradle.models

import java.sql.Time 
import java.sql.Date

class Medication(val pid: Int, val medication_id: Int, val amount: String, val start_date: Date, val end_date: Date, val times: Array<Time>)
