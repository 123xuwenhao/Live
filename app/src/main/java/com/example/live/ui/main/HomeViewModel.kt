package com.example.live.ui.main

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.live.logic.GoodsRepository
import com.example.live.logic.UploadHelper
import com.example.live.logic.model.Good

class HomeViewModel : ViewModel() {
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


    var imagesList = listOf<Bitmap>()
    private val operateUpload = MutableLiveData<Int>()

    val upLoadResponse = Transformations.switchMap(operateUpload) {
        UploadHelper.clear()
        val path = "/streaming"
        //val jsonString = "{\"path\":\"$path\"}"
        UploadHelper.addParameter("path", path)

        val files = UploadHelper.saveBitmapToFile(imagesList)
        if (files.size == 1)
            UploadHelper.addParameter(
                String.format("file\"; filename=\"" + files[0].name),
                files[0]
            )
        Log.e("params", UploadHelper.params.toString())
        GoodsRepository.uploadImage(UploadHelper.params)
    }


    fun upload() {
        if (operateUpload.value == null)
            operateUpload.postValue(0)
        else
            operateUpload.value = operateUpload.value!! + 1
    }
    /*
    fun upload(){
        val path = "/"
        val jsonString = "{\"path\":\"$path\"}"
        UploadHelper.addParameter("params", jsonString)
        UploadHelper.clear()
        val files = UploadHelper.saveBitmapToFile(imagesList)
        if (files.size == 1)
            UploadHelper.addParameter("file", files[0])
        Log.e("params", UploadHelper.params.toString())
        val temp = ServerService.serverService.upLoadImage(UploadHelper.params)
        temp.enqueue(object : Callback<UpLoadResponse> {
            override fun onFailure(call: Call<UpLoadResponse>, t: Throwable) {
                Log.e("fail","fail")
            }

            override fun onResponse(
                call: Call<UpLoadResponse>,
                response: Response<UpLoadResponse>
            ) {
                Log.e("success","success")
            }

        })
    }

     */
}