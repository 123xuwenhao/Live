package com.example.live.logic

import android.util.Log
import androidx.lifecycle.liveData
import com.example.live.logic.network.ServerService
import kotlinx.coroutines.Dispatchers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import kotlin.coroutines.CoroutineContext

object GoodsRepository {
    fun findGoodById(goodsId: Long) = make(Dispatchers.IO) {
        val responseData = ServerService.findGoodById(goodsId)
        if (responseData != null) Result.success(responseData)
        else Result.failure(RuntimeException())
    }

    fun findGoodsByCategory(category: String) = make(Dispatchers.IO) {
        val responseData = ServerService.findGoodsByCategory(category)
        if (responseData != null) Result.success(responseData)
        else Result.failure(RuntimeException())
    }

    fun findAllGoods() = make(Dispatchers.IO) {
        val responseData = ServerService.findAllGoods()
        if (responseData != null) Result.success(responseData)
        else Result.failure(RuntimeException())
    }

    fun addGoodToCart(userId: Long, goodsId: Long, quantity: Long) = make(Dispatchers.IO) {
        val responseData = ServerService.addGoodToCart(userId, goodsId, quantity)
        if (responseData != null) Result.success(responseData)
        else Result.failure(RuntimeException())
    }

    fun findAllGoodsInCart() = make(Dispatchers.IO) {
        val responseData = ServerService.findAllGoodsInCart()
        if (responseData != null) Result.success(responseData)
        else Result.failure(RuntimeException())
    }

    fun findUserInfoById(userId: Long) = make(Dispatchers.IO) {
        val responseData = ServerService.findUserInfoById(userId)
        if (responseData != null) Result.success(responseData)
        else Result.failure(RuntimeException())
    }

    fun submitOrder(
        userId: Long,
        deliveryId: Long,
        goodsId: Long,
        price: String,
        quantity: Long,
        payment: String,
        status: String
    ) = make(Dispatchers.IO) {
        val responseData =
            ServerService.submitOrder(userId, deliveryId, goodsId, price, quantity, payment, status)
        if (responseData != null) Result.success(responseData)
        else Result.failure(RuntimeException())
    }

    fun uploadImage(params: HashMap<String, RequestBody>) = make(Dispatchers.IO) {
        val responseData = ServerService.uploadImage(params)
        if (responseData != null) Result.success(responseData)
        else Result.failure(RuntimeException())
    }

    private fun <T> make(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData(context) {
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure(e)
            }
            emit(result)
        }
}