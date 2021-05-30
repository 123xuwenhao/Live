package com.example.live.ui.main

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.live.logic.LoginRepository
import com.example.live.logic.MyApplication
import com.example.live.logic.utils.CommonUtils

class LoginViewModel : ViewModel() {

    private val loginData = MutableLiveData<List<String>>() //登录请求的参数

    //登录请求响应
    val loginResponse = Transformations.switchMap(loginData){ param -> LoginRepository.login(param[0], param[1])
    }

    fun login(number: String, password: String) = when {
        number.length != 11 -> Toast.makeText(MyApplication.context, "非法的手机号", Toast.LENGTH_SHORT).show()
        password.length < 8 -> Toast.makeText(MyApplication.context, "非法的密码 ", Toast.LENGTH_SHORT).show()
        //TODO(else -> loginData.value = arrayListOf(number, CommonUtils.md5Encode(password)))
        else -> loginData.value = arrayListOf(number, CommonUtils.md5Encode(password))//密码MD5加密
    }
}