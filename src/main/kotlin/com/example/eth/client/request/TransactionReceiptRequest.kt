package com.example.eth.client.request


data class TransactionReceiptRequest(
    val jsonrpc: String,
    val method: String,
    val params: List<String>,
    val id: Int
)