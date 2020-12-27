package com.example.eth.client.response

data class BlockByNumberResponse(val jsonrpc: String, val id: Int, val result: EthBlock)

data class EthTransaction(
    val blockNumber: String,
    val blockHash: String,
    val transactionIndex: String,
    val from: String,
    val to: String,
    val hash: String,
)

data class EthBlock(
    val number: String,
    val hash: String,
    val difficulty: String,
    val totalDifficulty: String,
    var timestamp: String,
    var size: String,
    var gasUsed: String,
    var gasLimit: String,
    val transactions: List<EthTransaction>,
)