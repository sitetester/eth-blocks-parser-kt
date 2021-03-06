package com.example.eth

import com.example.eth.client.EthHttpClient
import com.example.eth.client.response.EthTransaction
import com.example.eth.entity.Block
import com.example.eth.entity.Transaction
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import org.springframework.stereotype.Component

@Component
class BlocksParser(private val ethHttpClient: EthHttpClient) {

    suspend fun parseBlockByNumber(number: Int): Block {

        val ethBlock = ethHttpClient.getBlockByNumber(number)

        val blockNumber = hexToLong(ethBlock.number)
        val transactions = ethBlock.transactions

        val block = Block(
            number = blockNumber,
            hash = ethBlock.hash,
            difficulty = hexToLong(ethBlock.difficulty),
            totalDifficulty = hexToLong(ethBlock.totalDifficulty),
            timestamp = hexToInt(ethBlock.timestamp),
            size = hexToInt(ethBlock.size),
            gasUsed = hexToLong(ethBlock.gasUsed),
            gasLimit = hexToLong(ethBlock.gasLimit),
            transactionsCount = transactions.count()
        )

        if (transactions.isNotEmpty()) {
            val statuses = getStatuses(transactions)
            block.transactions = getTransactions(ethBlock.transactions, statuses)
        }

        return block
    }

    /**
     * this code is using coroutine & channels, this is demonstration of how coroutines
     * communicate with each other (just like Golang coroutines & channels)
     *
     * https://kotlinlang.org/docs/tutorials/coroutines/coroutines-basic-jvm.html#async-returning-a-value-from-a-coroutine
     * we can also instead simply use `GlobalScope.async` to launch coroutines and collect their values
     * just like it's done in ImportManager::manageImport
     */
    private suspend fun getStatuses(transactions: List<EthTransaction>): MutableMap<String, String> {

        val hashStatusPairChannel = Channel<Pair<String, String>>()

        // launch a coroutine for fetching each transaction status, after status is fetched, send it on channel
        transactions.forEach { transaction ->
            GlobalScope.launch {
                hashStatusPairChannel.send(ethHttpClient.getTransactionReceipt(transaction.hash))
            }
        }

        val statuses: MutableMap<String, String> = mutableMapOf()
        // launch another coroutine to start receiving statuses from channel
        // `GlobalScope.launch` is like `fire and forget` a coroutine
        val job = GlobalScope.launch {
            for (pair in hashStatusPairChannel) {
                statuses[pair.first] = pair.second

                if (statuses.size == transactions.size) {
                    // close the channel to indicate no more values to send (kinda breaking this loop)
                    hashStatusPairChannel.close()
                }
            }
        }

        // this will make sure we are waiting for completion of this job
        job.join()

        return statuses
    }

    private fun getTransactions(
        ethTransactions: List<EthTransaction>,
        statuses: MutableMap<String, String>
    ): List<Transaction> {

        return ethTransactions.map { ethTransaction: EthTransaction ->
            var status = "Unknown"
            if (statuses.count() > 0 && statuses[ethTransaction.hash] != null) {
                status = statuses[ethTransaction.hash].toString()
            }
            Transaction(
                hash = ethTransaction.hash,
                blockNumber = hexToLong(ethTransaction.blockNumber),
                status = status,
                fromTx = ethTransaction.from,
                toTx = ethTransaction.to
            )
        }
    }

    // https://stackoverflow.com/questions/6611824/why-do-we-need-to-use-radix-parameter-when-calling-parseint
    private fun hexToInt(value: String): Int {

        return value.replace("0x", "").toInt(16)
    }

    private fun hexToLong(value: String): Long {

        return java.lang.Long.parseLong(value.replace("0x", ""), 16)
    }
}