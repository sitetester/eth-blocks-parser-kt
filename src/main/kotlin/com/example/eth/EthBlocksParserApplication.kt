package com.example.eth

import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EthBlocksParserApplication

fun main(args: Array<String>) {
    runApplication<EthBlocksParserApplication>(*args) {
        setBannerMode(Banner.Mode.OFF)
    }
}