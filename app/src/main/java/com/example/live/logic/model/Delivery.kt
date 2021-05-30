package com.example.live.logic.model

data class Delivery (
    val deliveryId: Long,
    val userId: Long,
    var name: String,
    var address: String,
    var phone: String,
    var postalAddress: String,
)

