package com.example.live.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.live.logic.GoodsRepository
import com.example.live.logic.model.Good

class TypeViewModel : ViewModel() {
    val typeListName = arrayListOf("推荐")
    var chosenTypePosition = MutableLiveData<Int>()
    var itemsList: List<Good> = mutableListOf()

    var getIntoDetailId = MutableLiveData<Long>()
    var getIntoLiveId = MutableLiveData<Long>()

    val findGoodsByCategory = Transformations.switchMap(chosenTypePosition) {
        if (it == 0)
            GoodsRepository.findAllGoods()
        else
            GoodsRepository.findGoodsByCategory(typeListName[chosenTypePosition.value!!])
    }
}