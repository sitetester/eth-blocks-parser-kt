package com.example.eth.repository

import com.example.eth.entity.Block
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BlocksRepository : CrudRepository<Block, Long> {

}