package com.example.live.logic.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object ServerService {
    val serverService = ServerCreator.create(ServerInterface::class.java)

    suspend fun login(phone: String, password: String) = serverService.login(phone, password).await()
    suspend fun signup(phone: String, password: String, type: String, token: String) = serverService.signup(phone, password, type, token).await()
    suspend fun modify(phone: String, password: String, type: String, token: String) = serverService.modify(phone, password, type, token).await()

    suspend fun findGoodById(goodsId: Long) = serverService.findGoodById(goodsId).await()
    suspend fun findAllGoods() = serverService.findAllGoods().await()
    suspend fun addGoodToCart(userId: Long, goodsId: Long, quantity: Long) =
        serverService.addGoodToCart(userId, goodsId, quantity).await()

    suspend fun findAllGoodsInCart() = serverService.findAllGoodsInCart().await()
    suspend fun findGoodsByCategory(category: String) =
        serverService.findGoodsByCategory(category).await()
    suspend fun findUserInfoById(userId: Long) = serverService.findUserInfoById(userId).await()

    suspend fun uploadImage(params: HashMap<String, RequestBody>) =
        serverService.upLoadImage(params).await()

    suspend fun submitOrder(
        userId: Long,
        deliveryId: Long,
        goodsId: Long,
        price: String,
        quantity: Long,
        payment: String,
        status: String
    ) = serverService.submitOrder(userId, deliveryId, goodsId, price, quantity, payment, status)
        .await()

    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) continuation.resume(body)
                    else continuation.resumeWithException(
                        RuntimeException("response body is null")
                    )
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }
}