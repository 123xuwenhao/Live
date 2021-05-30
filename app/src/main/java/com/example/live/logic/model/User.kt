package com.example.live.logic.model

import com.google.gson.annotations.SerializedName

data class User(val userId: Long,
                val type: String,
                val phone: String,
                val token: String,
                val password: String,)
