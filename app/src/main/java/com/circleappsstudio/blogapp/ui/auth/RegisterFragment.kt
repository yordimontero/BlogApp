package com.circleappsstudio.blogapp.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.util.PatternsCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.circleappsstudio.blogapp.R
import com.circleappsstudio.blogapp.core.Result
import com.circleappsstudio.blogapp.core.toast
import com.circleappsstudio.blogapp.data.remote.auth.AuthDataSource
import com.circleappsstudio.blogapp.databinding.FragmentRegisterBinding
import com.circleappsstudio.blogapp.domain.auth.AuthRepositoryImpl
import com.circleappsstudio.blogapp.presentation.auth.AuthViewModel
import com.circleappsstudio.blogapp.presentation.auth.AuthViewModelFactory

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var binding: FragmentRegisterBinding

    private val viewModel by viewModels<AuthViewModel> {

        AuthViewModelFactory(
            AuthRepositoryImpl(
                AuthDataSource()
            )
        )

    }

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

            if (validateUserCredentials(userName, email, password1, password2)) {
                return@setOnClickListener
            }

            signUpObserver(email, password1, userName)

        }

    }

    private fun signUpObserver(email: String, password: String, userName: String) {

        viewModel.signUp(email, password, userName)
            .observe(viewLifecycleOwner, Observer { resultEmitted ->

                when(resultEmitted) {

                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        findNavController().navigate(R.id.action_registerFragment_to_homeScreenFragment)
                        binding.progressBar.visibility = View.GONE
                    }

                    is Result.Failure -> {

                        requireContext().toast(
                            requireContext(),
                            "Something went wrong: ${resultEmitted.exception}",
                            Toast.LENGTH_SHORT
                        )

                        binding.progressBar.visibility = View.GONE
                    }

                }

            })

    }

    private fun validateUserCredentials(
        userName: String,
        email: String,
        password1: String,
        password2: String
    ): Boolean {

        if (userName.isEmpty()) {
            binding.txtUserName.error = "Username empty!"
            return true
        }

        if (email.isEmpty()) {
            binding.txtEmail.error = "E-mail empty!"
            return true
        }

        if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.txtEmail.error = "Invalid e-mail!"
            return true
        }

        if (password1.isEmpty()) {
            binding.txtPassword1.error = "Password empty!"
            return true
        }

        if (password2.isEmpty()) {
            binding.txtPassword2.error = "Password empty!"
            return true
        }

        if (password1 != password2) {
            binding.txtPassword1.error = "Passwords does not match!"
            binding.txtPassword2.error = "Passwords does not match!"
            return true
        }

        return false

    }

}