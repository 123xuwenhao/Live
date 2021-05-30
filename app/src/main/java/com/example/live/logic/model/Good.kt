package com.example.live.logic.model

data class Good(val goodsId: Long,
                val vendorId: Long,
                val name: String,
                val price: String,
                val category: String,
                val soldCount: Long,
                val maxQuantity: Long,
                val photoPath: String,
                val descriptionPath: String,
                val specPath: String,
                val deliveryType: String,
                val displayType: String,)
