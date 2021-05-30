package com.example.live.logic

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.os.Environment
import android.util.Log
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.Exception
import kotlin.collections.HashMap
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

object UploadHelper {
    var params: HashMap<String, RequestBody> = HashMap()

    fun addParameter(key: String, o: Any): HashMap<String, RequestBody>{
        lateinit var body: RequestBody
        if (o is String)
            body = o.toRequestBody("text/plain;charset=UTF-8".toMediaTypeOrNull())
        else if (o is File)
            body = o.asRequestBody("multipart/form-data;charset=UTF-8".toMediaTypeOrNull())
        params.put(key, body)
        return params
    }

    fun clear(){
        params.clear()
    }

    fun saveBitmapToFile(images: List<Bitmap>): List<File> {
        val root = Environment.getExternalStorageDirectory().absolutePath.toString()
        val files = mutableListOf<File>()
        val myDir = File(root, "live")
        var boo = myDir.exists()
        Log.e("mkdir", boo.toString())
        if (myDir.exists())
            myDir.delete()
        boo = myDir.mkdirs()
        Log.e("mkdir", boo.toString())
        for (i in images.indices){
            files.add(i, File(myDir, "ims-$i.JPEG"))
            Log.e("test",files[i].toString())
            var bool = files[i].exists()
            Log.e("fileExist",bool.toString())
            if (bool) {
                bool = files[i].delete()
                Log.e("fileDelete",bool.toString())
            }
            try {
                bool = files[i].createNewFile()
                Log.e("fileCreate",bool.toString())
                val out = FileOutputStream(files[i])
                images[i].compress(Bitmap.CompressFormat.JPEG, 30, out)
                out.flush()
                out.close()
                Log.e("最终上传图片的路径", files[i].absolutePath)
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
        return files.toList()
    }
}