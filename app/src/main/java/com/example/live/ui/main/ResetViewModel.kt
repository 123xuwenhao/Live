package com.example.live.ui.main

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.live.logic.LoginRepository
import com.example.live.logic.MyApplication
import com.example.live.logic.utils.CommonUtils
import kotlin.random.Random

class ResetViewModel : ViewModel()  {

    private lateinit var resetValidCode: String //验证码
    private val resetData = MutableLiveData<List<String>>() //重置密码的参数

    //响应重置密码的请求
    val resetResponse = Transformations.switchMap(resetData) { param ->
        LoginRepository.modify(param[0], param[1], "0","0")
    }

    fun reset(phoneNumber: String, password: String, password2: String, validCode: String) = when {
        phoneNumber.length != 11 -> Toast.makeText(MyApplication.context, "非法的手机号", Toast.LENGTH_SHORT).show()
        password.length < 8 -> Toast.makeText(MyApplication.context, "请输入至少8位密码 ", Toast.LENGTH_SHORT).show()
        password != password2 -> Toast.makeText(MyApplication.context, "两次输入的密码不一致 ", Toast.LENGTH_SHORT).show()
        validCode != resetValidCode -> Toast.makeText(MyApplication.context, "验证码错误 ", Toast.LENGTH_SHORT).show()
        else -> resetData.value = arrayListOf(phoneNumber, CommonUtils.md5Encode(password))
    }

    fun sendSMS(phoneNumber: String){
        String.format("%04d", Random.nextInt(9999)).also { resetValidCode = it }
        LoginRepository.sendSMS(phoneNumber, resetValidCode)
    }
}