package com.example.live.logic.network

import com.example.live.logic.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ServerInterface {
    @GET("login")
    fun login(@Query("phone")phone: String, @Query("password")password: String): Call<User>

    @GET("signup")
    fun signup(@Query("phone")phone: String, @Query("password")password: String, @Query("type")type: String, @Query("token")token: String): Call<User>

    @GET("modify")
    fun modify(@Query("phone")phone: String, @Query("password")password: String, @Query("type")type: String, @Query("token")token: String): Call<User>

    @GET("goods/find")
    fun findGoodById(@Query("goods_id") goodsId: Long): Call<Good?>

    @GET("goods/find_all")
    fun findAllGoods(): Call<GoodsList?>

    @GET("goods/find_by_category")
    fun findGoodsByCategory(@Query("category") category: String): Call<GoodsList?>

    @GET("cart/add")
    fun addGoodToCart(@Query(value = "user_id")userId: Long,@Query(value = "goods_id")goodsId: Long,@Query(value = "quantity")quantity: Long): Call<CartWithUserId?>

    @GET("cart/find_all")
    fun findAllGoodsInCart(): Call<List<CartWithUserId>?>

    @Multipart
    @POST("upload")
    fun upLoadImage(@PartMap params: HashMap<String, RequestBody>): Call<UpLoadResponse>

    @GET("user_info/find")
    fun findUserInfoById(@Query(value="user_id")userId: Long): Call<UserInfo>

    @GET("user_order/add")
    fun submitOrder(@Query(value = "user_id")userId: Long,
                    @Query(value = "delivery_id")deliveryId: Long,
                    @Query(value = "goods_id")goodsId: Long,
                    @Query(value = "price")price: String,
                    @Query(value = "quantity")quantity: Long,
                    @Query(value = "payment")payment: String,
                    @Query(value = "status")status: String): Call<Order>
}