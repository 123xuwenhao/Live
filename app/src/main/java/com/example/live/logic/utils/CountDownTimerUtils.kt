package com.example.live.logic.utils

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.CountDownTimer
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import com.example.live.R
import com.example.live.logic.utils.CommonUtils.getString

class CountDownTimerUtils (private val textView: TextView, duration: Long, countDownInterval: Long) : CountDownTimer(duration, countDownInterval) {

    @SuppressLint("SetTextI18n")
    override fun onTick(duration: Long) {
        textView.isClickable = false
        with(textView) {
            text = String.format("(%02ds)", duration/1000)
            //setBackgroundResource(com.example.superstar_v10.R.drawable.validate_code_press_bg)
        }
        val span = ForegroundColorSpan(Color.BLACK)
        val spannableString = SpannableString(textView.text.toString()).apply {
            setSpan(span, 0, 5, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        }
        textView.text = spannableString
    }

    override fun onFinish() {
        textView.text = "发送"
        textView.isClickable = true
        //textView.setBackgroundResource(com.example.superstar_v10.R.drawable.log_rectangle_4_8)
    }
}