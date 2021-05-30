package com.example.live.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.live.logic.GoodsRepository
import com.example.live.logic.model.CartInfo
import com.example.live.logic.model.CartWithUserId

class CartViewModel : ViewModel() {
    var goodsList: List<CartInfo> = listOf()

    private var operateFindAllCarts = MutableLiveData<Int>()

    var sum = MutableLiveData<Double>()
    var count = MutableLiveData<Int>()

    val findAllCartsResponse = Transformations.switchMap(operateFindAllCarts) {
        GoodsRepository.findAllGoodsInCart()
    }

    fun getAllCarts() {
        if (operateFindAllCarts.value == null)
            operateFindAllCarts.postValue(0)
        else
            operateFindAllCarts.value = operateFindAllCarts.value!! + 1
    }

    fun freshGoodsList(webResult: List<CartWithUserId>): List<CartInfo> {
        return webResult.map {
            CartInfo(
                it.goodsId,
                "name",
                "price",
                it.quantity,
                "Path",
                1000L,
                false
            )
        }
        //TODO(需要设置)
    }

    fun notifySum() {
        sum.value = goodsList.fold(
            0.0,
            operation = { acc: Double, cartInfo: CartInfo -> if (cartInfo.isChecked) acc + cartInfo.price.toDouble() * cartInfo.quantity else acc })
        count.value = goodsList.fold(
            0,
            operation = { acc: Int, cartInfo: CartInfo -> if (cartInfo.isChecked) acc + 1 else acc })
    }
}