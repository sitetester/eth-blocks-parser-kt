package com.example.eth.client.response


data class TransactionReceipt(
    val transactionHash: String,
    val transactionIndex: String,
    val blockHash: String,
    val blockNumber: String,
    val status: String
)

data class TransactionReceiptResponse(val jsonrpc: String, val id: Int, val result: TransactionReceipt)
