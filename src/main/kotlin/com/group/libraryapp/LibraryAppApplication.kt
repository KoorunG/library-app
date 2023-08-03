package com.group.libraryapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LibraryAppApplication
fun main(args: Array<String>) {
//    SpringApplication.run(LibraryAppApplication::class.java, *args)
    runApplication<LibraryAppApplication>(*args)
}
