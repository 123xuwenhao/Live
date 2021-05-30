package com.example.live.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.live.logic.DataManager
import com.example.live.logic.GoodsRepository

class OrderViewModel : ViewModel() {
    data class GoodsList(
        val goodsId: Long,
        val goodsName: String?,
        val goodsPrice: String?,
        val goodsPhoto: String?,
        var quantity: Long,
        val maxQuantity: Long
    )

    var goodsList: List<GoodsList> = listOf()
    private val userId = DataManager.loginId

    var sumText = MutableLiveData<String>()

    private var operateSubmit = MutableLiveData<Int>()

    val submitResponse = Transformations.switchMap(operateSubmit) {
        if (goodsList.isNotEmpty()) {
            with(goodsList[0]) {
                GoodsRepository.submitOrder(
                    userId,
                    0,
                    this.goodsId,
                    if (this.goodsPrice.isNullOrEmpty()) "0.0" else this.goodsPrice.toString(),
                    this.quantity,
                    "未付款",
                    "待发货"
                )
            }
        } else
            null
        //TODO(需要修改为多商品订单提交)
    }

    fun submitOrder() {
        if (operateSubmit.value == null)
            operateSubmit.postValue(0)
        else
            operateSubmit.value = operateSubmit.value!! + 1
    }

    fun notifySum() {
        if (goodsList.isNotEmpty()) {
            val sum = goodsList.fold(0.0, { acc: Double, goodsList: GoodsList ->
                acc + (goodsList.goodsPrice?.toDouble() ?: 0.0) * goodsList.quantity
            })
            sumText.value = "合计：$sum"
        } else {
            sumText.value = "合计：0.00"
        }
    }
}