package com.backend.remindmedapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan


@SpringBootApplication
@ComponentScan("com.backend.remindmedapi.controllers")
class RemindmedApiApplication

fun main(args: Array<String>) {
	runApplication<RemindmedApiApplication>(*args)
}
