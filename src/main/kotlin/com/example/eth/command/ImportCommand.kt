package com.example.eth.command

import com.example.eth.ImportManager
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod

@ShellComponent
class ImportCommand(private val importManager: ImportManager) {

    @ShellMethod("Imports Ethereum Blockchain blocks & transactions status")
    fun import() {
        return importManager.manageImport()
    }
}