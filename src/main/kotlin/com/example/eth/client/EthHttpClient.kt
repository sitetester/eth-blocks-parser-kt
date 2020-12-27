package com.example.eth.client

import com.example.eth.client.request.BlockByNumberRequest
import com.example.eth.client.request.TransactionReceiptRequest
import com.example.eth.client.response.BlockByNumberResponse
import com.example.eth.client.response.EthBlock
import com.example.eth.client.response.TransactionReceiptResponse
import com.google.gson.Gson
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.util.EntityUtils
import org.springframework.stereotype.Component

@Component
class EthHttpClient {

    private val remoteUrl = "https://mainnet.infura.io/v3/" + System.getenv("INFURA_PROJECT_ID")

    fun getBlockByNumber(number: Int): EthBlock {

        val blockByNumberRequest = BlockByNumberRequest(
            "2.0",
            "eth_getBlockByNumber",
            mutableListOf("0x" + ("%x".format(number)).toUpperCase(), true),
            1
        )

        val responseJson = postRequestWithJsonString(Gson().toJson(blockByNumberRequest))
        val blockByNumberResponse = Gson().fromJson(responseJson, BlockByNumberResponse::class.java)

        return blockByNumberResponse.result
    }

    fun getTransactionReceipt(hash: String): Pair<String, String> {

        val transactionReceiptRequest = TransactionReceiptRequest(
            "2.0",
            "eth_getTransactionReceipt",
            listOf(hash),
            1
        )

        val responseString = postRequestWithJsonString(Gson().toJson(transactionReceiptRequest))
        val transactionReceiptResponse = Gson().fromJson(responseString, TransactionReceiptResponse::class.java)

        return Pair(hash, transactionReceiptResponse.result.status)
    }

    private fun postRequestWithJsonString(json: String): String {

        val httpPost = HttpPost(remoteUrl)
        httpPost.setHeader("Content-type", "application/json")
        httpPost.setHeader("Accept", "application/json")

        httpPost.entity = StringEntity(json)
        val client = DefaultHttpClient()
        val response = client.execute(httpPost)

        return EntityUtils.toString(response.entity)
    }
}



