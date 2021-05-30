package com.example.live.logic.utils

import android.preference.PreferenceManager
import android.widget.Toast
import com.example.live.logic.MyApplication
import java.security.MessageDigest
import java.util.*


object CommonUtils {

    //Toast简化
    fun toastShort(string: String){
        Toast.makeText(MyApplication.context, string, Toast.LENGTH_SHORT).show()
    }
    fun toastShort(int : Int){
        Toast.makeText(MyApplication.context, MyApplication.context.getString(int), Toast.LENGTH_SHORT).show()
    }


    //类文件获取string
    fun getString(int : Int) = MyApplication.context.getString(int)
    fun getDrawble(int :Int) = MyApplication.context.getDrawable(int)

    //单向加密字符串
    fun md5Encode(raw: String): String {
        val code = MessageDigest.getInstance("MD5").digest(raw.toByteArray())
        var result = ""
        var char = ""
        for (n in code.indices) {
            char = Integer.toHexString(code[n].toInt() and 255)
            result = if (char.length == 1) {
                result + "0" + char
            } else {
                result + char
            }
        }
        return result.toUpperCase(Locale.getDefault())
    }

    //获取MD5加密的UUID
    fun getUuid(): String {
        val key = "key_uuid"
        val preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.context)
        var uuid: String? = preferences.getString(key, "")
        if (uuid.isNullOrEmpty()) {
            uuid = md5Encode(UUID.randomUUID().toString())
            preferences?.edit()?.putString(key, uuid)?.apply()
        }
        return uuid
    }
}