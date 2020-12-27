package com.example.eth

// @Component
class AppStartupRunner(private val importManager: ImportManager) {

    // @PostConstruct
    fun init() {
        importManager.manageImport()
    }
}