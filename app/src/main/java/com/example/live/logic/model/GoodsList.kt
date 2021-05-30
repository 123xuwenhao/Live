package com.example.live.logic.model

data class GoodsList(
    val content: List<Good>,
    val pageable: String,
    val last: Boolean,
    val totalPages: Int,
    val totalElements: Int,
    val size: Int,
    val number: Int,
    val first: Boolean,
    val numberOfElements: Int,
    val empty: Boolean
)
