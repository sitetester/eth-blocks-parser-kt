package com.example.eth.repository

import com.example.eth.entity.Transaction
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TransactionsRepository : CrudRepository<Transaction, Long> {

}