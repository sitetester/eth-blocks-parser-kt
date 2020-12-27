package com.example.eth

import com.example.eth.entity.Block
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.hibernate.SessionFactory
import org.springframework.stereotype.Component

// https://stackoverflow.com/questions/35479631/how-to-use-spring-annotations-like-autowired-in-kotlin
// https://stackoverflow.com/questions/6827752/whats-the-difference-between-component-repository-service-annotations-in
@Component
class ImportManager(
    private val sessionFactory: SessionFactory,
    private val blocksParser: BlocksParser
) {

    // https://stackoverflow.com/questions/46226518/what-is-the-difference-between-launch-join-and-async-await-in-kotlin-coroutines
    // https://dzone.com/articles/waiting-for-coroutines
    fun manageImport(startingBlockNumber: Int = 50000) {

        val maxNum = startingBlockNumber + 10
        val blockNumbers = (IntRange(startingBlockNumber, maxNum)).toList()
        println("Parsing blocks range: ${blockNumbers.first()} - ${blockNumbers.last()}")

        runBlocking {
            val parsedBlocks = blockNumbers.map { blockNumber ->
                // https://kotlinlang.org/docs/tutorials/coroutines/coroutines-basic-jvm.html#async-returning-a-value-from-a-coroutine
                // `GlobalScope.async` is like `start a coroutine that computes some result`
                GlobalScope.async {
                    blocksParser.parseBlockByNumber(blockNumber)
                }
            }

            val resolvedBlocks = parsedBlocks.map { it.await() }
            bulkInsert(resolvedBlocks)
        }

        manageImport(maxNum)
    }

    // https://stackoverflow.com/questions/7349464/bulk-insert-or-update-with-hibernate
    private fun bulkInsert(blocks: List<Block>) {

        val session = sessionFactory.openSession()
        val tx = session.beginTransaction()

        blocks.forEach { block ->
            session.save(block)
        }

        session.flush()
        session.clear()

        tx.commit()
        session.close()
    }
}