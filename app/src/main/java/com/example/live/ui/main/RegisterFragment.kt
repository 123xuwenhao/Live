package com.example.live.ui.main

import android.content.Intent
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
import com.example.live.MainActivity
import com.example.live.databinding.LoginRegisterBinding
import com.example.live.logic.DataManager
import com.example.live.logic.utils.CountDownTimerUtils

//注册页
class RegisterFragment : Fragment() {

    private lateinit var viewModel: RegisterViewModel
    private var _binding: LoginRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LoginRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        viewModel.registerResponse.observe(viewLifecycleOwner) { registerResult ->
            val result = registerResult.getOrNull()
            if (result != null) {
                DataManager.loginId = result.userId
                Toast.makeText(this.context, "欢迎", Toast.LENGTH_SHORT).show()
                Intent(activity, MainActivity::class.java).apply {
                    startActivity(this)
                }
                this.requireActivity().finish()
            } else {
                Toast.makeText(this.context, "注册失败", Toast.LENGTH_SHORT).show()
            }
        }

        //发送验证码按键，同时启动倒计时
        binding.regBtnValid.setOnClickListener {
            if (binding.regInUser.text.toString().length != 11)
                Toast.makeText(this.context, "验证码已发送", Toast.LENGTH_SHORT).show()
            else {
                viewModel.sendSMS(binding.regInUser.text.toString())
                CountDownTimerUtils(binding.regBtnValid, 60000, 1000).apply { start() }
            }
        }

        binding.regInPwd.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    if (s.isNotEmpty()) {
                        val str = s.toString()
                        val lastChar = str.last()
                        if (lastChar in '0'..'9' || lastChar in 'a'..'z' || lastChar in 'A'..'Z')
                            return
                        else
                            s.delete(str.length - 1, str.length)
                    }
                }
            }
        })

        //输入密码时允许确认键发送请求
        binding.regInPwd.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.register(
                    binding.regInUser.text.toString(),
                    binding.regInPwd.text.toString(),
                    binding.regInValid.text.toString(),
                )
            }
            false
        }

        //注册键
        binding.regBtnReg.setOnClickListener {
            viewModel.register(
                binding.regInUser.text.toString(),
                binding.regInPwd.text.toString(),
                binding.regInValid.text.toString(),
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}