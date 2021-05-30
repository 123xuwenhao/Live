package com.example.live.logic.model

data class Order (
    private val orderId: Long,
    private val userId: Long,
    private val deliveryId: Long,
    private val goodsId: Long,
    private val price: Long,
    private val quantity: Long,
    private val payment: String,
    private val status: String,
)