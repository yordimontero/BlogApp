package com.circleappsstudio.blogapp.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.util.PatternsCompat
import com.circleappsstudio.blogapp.R
import com.circleappsstudio.blogapp.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var binding: FragmentRegisterBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentRegisterBinding.bind(view)

        signUp()

    }

    private fun signUp() {

        binding.btnSignUp.setOnClickListener {

            val userName = binding.txtUserName.text.toString().trim()
            val email = binding.txtEmail.text.toString().trim()
            val password1 = binding.txtPassword1.text.toString().trim()
            val password2 = binding.txtPassword2.text.toString().trim()

            if (userName.isEmpty()) {
                binding.txtUserName.error = "Username empty!"
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                binding.txtEmail.error = "E-mail empty!"
                return@setOnClickListener
            }

            if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.txtEmail.error = "Invalid e-mail!"
                return@setOnClickListener
            }

            if (password1.isEmpty()) {
                binding.txtPassword1.error = "Password empty!"
                return@setOnClickListener
            }

            if (password2.isEmpty()) {
                binding.txtPassword2.error = "Password empty!"
                return@setOnClickListener
            }

            if (password1 != password2) {
                binding.txtPassword1.error = "Passwords does not match!"
                binding.txtPassword2.error = "Passwords does not match!"
                return@setOnClickListener
            }

        }

    }

}