package com.example.eth.client.request


data class BlockByNumberRequest(
    val jsonrpc: String,
    val method: String,
    val params: List<Any>,
    val id: Int
)

data class TransactionReceiptRequest(
    val jsonrpc: String,
    val method: String,
    val params: List<String>,
    val id: Int
)