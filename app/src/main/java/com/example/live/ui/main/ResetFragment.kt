package com.example.live.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.live.R
import com.example.live.databinding.LoginResetBinding
import com.example.live.logic.MyApplication
import com.example.live.logic.utils.CountDownTimerUtils

//忘记密码
class ResetFragment : Fragment() {

    private lateinit var viewModel: ResetViewModel
    private var _binding: LoginResetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = LoginResetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ResetViewModel::class.java)

        //观察重置密码请求结果
        viewModel.resetResponse.observe(viewLifecycleOwner, { resetResult ->
            val result = resetResult.getOrNull()
            if (result != null) {
                Toast.makeText(MyApplication.context, "重置密码成功 ", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_resetFragment_to_loginFragment)
            } else {
                Toast.makeText(MyApplication.context, "重置密码失败 ", Toast.LENGTH_SHORT).show()
            }
        })

        //发送验证码并开始倒计时
        binding.rsBtnValid.setOnClickListener {
            if (binding.rsInUser.text.toString().length != 11)
                Toast.makeText(MyApplication.context, "请输入合法的手机号 ", Toast.LENGTH_SHORT).show()
            else{
                viewModel.sendSMS(binding.rsInUser.text.toString())
                CountDownTimerUtils(binding.rsBtnValid, 60000, 1000).apply { start() }
            }
        }

        binding.rsInPwd.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    if (s.isNotEmpty()){
                        val str = s.toString()
                        val lastChar = str.last()
                        if (lastChar in '0'..'9' || lastChar in 'a'..'z' || lastChar in 'A'..'Z')
                            return
                        else
                            s.delete(str.length-1, str.length)
                    }
                }
            }
        })

        binding.rsInConfirmPwd.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    if (s.isNotEmpty()){
                        val str = s.toString()
                        val lastChar = str.last()
                        if (lastChar in '0'..'9' || lastChar in 'a'..'z' || lastChar in 'A'..'Z')
                            return
                        else
                            s.delete(str.length-1, str.length)
                    }
                }
            }
        })

        //重复密码时允许确认键发送请求
        binding.rsInConfirmPwd.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.reset(
                    binding.rsInUser.text.toString(),
                    binding.rsInPwd.text.toString(),
                    binding.rsInConfirmPwd.text.toString(),
                    binding.rsInValid.text.toString()
                )
            }
            false
        }

        //忘记密码按键
        binding.rsBtnRs.setOnClickListener {
            viewModel.reset(
                binding.rsInUser.text.toString(),
                binding.rsInPwd.text.toString(),
                binding.rsInConfirmPwd.text.toString(),
                binding.rsInValid.text.toString()
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
