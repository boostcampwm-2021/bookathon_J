package com.tyehooney.fedyourpet.ui

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.tyehooney.fedyourpet.databinding.LoginFragmentBinding
import com.tyehooney.fedyourpet.util.sendVerifyingCode
import com.tyehooney.fedyourpet.util.signInWithVerifyingCode

class LoginFragment : Fragment(), LoginListener {

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: LoginFragmentBinding

    private lateinit var sharedPreferences: SharedPreferences
    private var verificationId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LoginFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            sharedPreferences = requireActivity().getSharedPreferences("userInfo", MODE_PRIVATE)
            if (sharedPreferences.getString("uid", null) == null) {
                binding.loginLinearLayout.visibility = View.VISIBLE
            } else {
                // 다음 화면 이동
            }
        }, 2000)

        binding.receiveCodeButton.setOnClickListener {
            val phoneNum = binding.phoneEditText.text.toString()
            if (phoneNum.isNotEmpty()) {
                binding.progressBar.visibility = View.VISIBLE
                sendVerifyingCode("+82$phoneNum", requireActivity(), this)
            }
        }

        binding.signInButton.setOnClickListener {
            val verifyingCode = binding.verifyingCodeEditText.text.toString()
            if(verificationId.isNotEmpty()) {
                binding.progressBar.visibility = View.VISIBLE
                signInWithVerifyingCode(verificationId, verifyingCode, this)
            }
        }
    }

    override fun onLoginSuccess(uid: String) {
        binding.progressBar.visibility = View.GONE
        val editor = sharedPreferences.edit()
        editor.putString("uid", uid).apply()
    }

    override fun onVerifyingCodeReceived(verificationId: String) {
        binding.progressBar.visibility = View.GONE
        this.verificationId = verificationId
    }

    override fun onLoginFailed(msg: String) {
        binding.progressBar.visibility = View.GONE
        Toast.makeText(requireContext(), "login failed!! : $msg", Toast.LENGTH_SHORT).show()
    }
}