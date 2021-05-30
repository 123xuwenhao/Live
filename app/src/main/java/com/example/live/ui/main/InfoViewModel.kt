package com.example.live.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.live.logic.GoodsRepository

class InfoViewModel : ViewModel() {
    val userId = MutableLiveData<Long>()

    val getUserInfo = Transformations.switchMap(userId) {
        GoodsRepository.findUserInfoById(it)
    }
}