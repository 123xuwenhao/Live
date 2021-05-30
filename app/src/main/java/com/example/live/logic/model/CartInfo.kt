package com.example.live.logic.model

data class CartInfo (
    val goodsId: Long,
    val name: String,
    val price: String,
    var quantity: Long,
    val photoPath: String,
    val maxQuantity: Long,
    var isChecked: Boolean)