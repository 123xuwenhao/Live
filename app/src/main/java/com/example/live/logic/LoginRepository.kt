package com.example.live.logic

import android.widget.Toast
import androidx.lifecycle.liveData
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.live.logic.network.AliyunService
import com.example.live.logic.network.ServerService
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

object LoginRepository {

    fun login(phone: String, password: String) = make(Dispatchers.IO) {
        val responseData = ServerService.login(phone, password)
        if (responseData.userId != null) Result.success(responseData)
        else Result.failure(RuntimeException())
    }

    fun signup(phone: String, password: String, type: String, token: String) = make(Dispatchers.IO) {
        val responseData = ServerService.signup(phone, password, type, token)
        if (responseData.userId != null) Result.success(responseData)
        else Result.failure(RuntimeException())
    }

    fun modify(phone: String, password: String, type: String, token: String) = make(Dispatchers.IO) {
        val responseData = ServerService.modify(phone, password, type, token)
        if (responseData.userId != null) Result.success(responseData)
        else Result.failure(RuntimeException())
    }

    fun logOut() {
        //TODO()
    }

    fun sendSMS(phoneNumber: String, validCode: String) {
        val url = AliyunService.requireSignature(phoneNumber, validCode)
        val queue = Volley.newRequestQueue(MyApplication.context)
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { Toast.makeText(MyApplication.context, "发送成功", Toast.LENGTH_SHORT).show() },
            { Toast.makeText(MyApplication.context, "发送失败", Toast.LENGTH_SHORT).show() })
        queue.add(stringRequest)
    }
    
    private fun <T> make(context: CoroutineContext, block: suspend () -> Result<T>) = liveData(context) {
        val result = try {
            block()
        } catch (e: Exception) {
            Result.failure(e)
        }
        emit(result)
    }
}

