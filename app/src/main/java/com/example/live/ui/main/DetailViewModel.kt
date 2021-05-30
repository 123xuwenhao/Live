package com.example.live.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.live.logic.DataManager
import com.example.live.logic.GoodsRepository

class DetailViewModel : ViewModel() {

    //data from other page
    var goodsId = MutableLiveData<Long>()
    var name = MutableLiveData<String>()
    var price = MutableLiveData<String>()

    //data from internet
    var vendorId = MutableLiveData<Long>()
    var category = MutableLiveData<String>()
    var soldCount = MutableLiveData<Long>()
    var maxQuantity = MutableLiveData<Long>()
    var photoPath = MutableLiveData<String>()
    var descriptionPath = MutableLiveData<String>()
    var specPath = MutableLiveData<String>()
    var deliveryType = MutableLiveData<String>()
    var displayType = MutableLiveData<String>()

    private var operateAddCart = MutableLiveData<Int>()

    val detailResponse = Transformations.switchMap(goodsId) {
        GoodsRepository.findGoodById(it)
    }

    val addCartResponse = Transformations.switchMap(operateAddCart) {
        if (goodsId.value != null && maxQuantity.value != null && maxQuantity.value != 0L) {
            GoodsRepository.addGoodToCart(DataManager.loginId, goodsId.value!!, 1)
        } else
            null
    }

    fun addGoodsToCart() {
        if (operateAddCart.value == null)
            operateAddCart.postValue(0)
        else
            operateAddCart.value = operateAddCart.value!! + 1
    }
}