package com.example.eth.entity

import javax.persistence.*

// https://spring.io/guides/tutorials/spring-boot-kotlin/
@Entity
@Table(name = "blocks")
class Block(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var number: Long,
    var hash: String? = null,

    var difficulty: Long,
    var totalDifficulty: Long,

    var size: Int,
    var gasUsed: Long? = null,
    var gasLimit: Long? = null,

    var transactionsCount: Int? = null,
    var timestamp: Int? = null,

    // `cascade` option must be set in order to save this relationship data successfully
    @OneToMany(cascade = [CascadeType.PERSIST], mappedBy = "block", fetch = FetchType.LAZY)
    var transactions: List<Transaction>? = emptyList(),
)