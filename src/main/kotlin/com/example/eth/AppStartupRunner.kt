package com.example.eth

import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class AppStartupRunner(private val importManager: ImportManager) {

    @PostConstruct
    fun init() {
        importManager.manageImport()
    }
}