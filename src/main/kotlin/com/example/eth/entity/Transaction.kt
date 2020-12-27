package com.example.eth.entity

import javax.persistence.*

@Entity
@Table(name = "transactions")
class Transaction(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var hash: String? = null,
    var blockNumber: Long,

    var status: String? = null,

    var fromTx: String? = null,
    var toTx: String? = null,

    @ManyToOne
    @JoinColumn(name = "block_id")
    var block: Block? = null
)