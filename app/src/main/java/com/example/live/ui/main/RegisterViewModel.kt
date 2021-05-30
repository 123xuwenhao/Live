package com.example.live.ui.main

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.live.logic.LoginRepository
import com.example.live.logic.MyApplication
import com.example.live.logic.utils.CommonUtils
import kotlin.random.Random

class RegisterViewModel : ViewModel() {

    private lateinit var registerValidCode: String //验证码
    private val registerData = MutableLiveData<List<String>>() //注册请求参数

    //响应注册请求
    val registerResponse = Transformations.switchMap(registerData) { param ->
        LoginRepository.signup(param[0], param[1], "0","0")
    }

    //注册
    fun register(
        phoneNumber: String,
        password: String,
        validCode: String,
    ) = when {
        phoneNumber.length != 11 -> Toast.makeText(MyApplication.context, "非法的手机号", Toast.LENGTH_SHORT).show()
        password.length < 8 -> Toast.makeText(MyApplication.context, "请输入至少8位密码 ", Toast.LENGTH_SHORT).show()
        validCode != registerValidCode -> Toast.makeText(MyApplication.context, "验证码错误 ", Toast.LENGTH_SHORT).show()
        else -> registerData.value = arrayListOf(phoneNumber, CommonUtils.md5Encode(password))//密码MD5加密
        }

    //发送验证码
    fun sendSMS(phoneNumber: String) {
        String.format("%04d", Random.nextInt(9999)).also { registerValidCode = it }
        Log.e("validCode", registerValidCode)
        LoginRepository.sendSMS(phoneNumber, registerValidCode)
    }
}
