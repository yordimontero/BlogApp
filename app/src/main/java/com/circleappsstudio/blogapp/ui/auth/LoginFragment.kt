package com.circleappsstudio.blogapp.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.util.PatternsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.circleappsstudio.blogapp.R
import com.circleappsstudio.blogapp.core.Resource
import com.circleappsstudio.blogapp.data.remote.auth.LoginDataSource
import com.circleappsstudio.blogapp.databinding.FragmentLoginBinding
import com.circleappsstudio.blogapp.domain.auth.LoginRepositoryImpl
import com.circleappsstudio.blogapp.presentation.auth.LoginScreenViewModel
import com.circleappsstudio.blogapp.presentation.auth.LoginScreenViewModelFactory
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val viewModel by viewModels<LoginScreenViewModel> {
        LoginScreenViewModelFactory(
            LoginRepositoryImpl(
                LoginDataSource()
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLoginBinding.bind(view)

        isUserLoggedIn()

        doLogIn()

        goToSignUp()

    }

    private fun isUserLoggedIn() {
        firebaseAuth.currentUser?.let {
            goToHomeScreen()
        }
    }

    private fun doLogIn() {

        binding.btnLogin.setOnClickListener {

            val email = binding.txtEmail.text.toString().trim()
            val password = binding.txtPassword.text.toString().trim()

            validateCredentials(email, password)

        }

    }

    private fun validateCredentials(email: String, password: String) {

        if (email.isEmpty()) {
            binding.txtEmail.error = "E-mail is empty!"
            return
        }

        if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.txtEmail.error = "E-mail is invalid!"
            return
        }

        if (password.isEmpty()) {
            binding.txtPassword.error = "Password is empty!"
            return
        }

        if (password.length < 5) {
            binding.txtPassword.error = "Password must have 6 or more characters!"
            return
        }

        signIn(email, password)

    }

    private fun signIn(email: String, password: String) {

        viewModel.signIn(email, password)
            .observe(viewLifecycleOwner, Observer { resultEmitted ->

                when(resultEmitted) {

                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Resource.Success -> {
                        goToHomeScreen()
                        binding.progressBar.visibility = View.GONE
                    }

                    is Resource.Failure -> {

                        Toast.makeText(
                            requireContext(),
                            "Sign In error: ${resultEmitted.exception.message}",
                            Toast.LENGTH_LONG
                        ).show()

                        binding.progressBar.visibility = View.GONE

                    }

                }

            })

    }

    private fun goToHomeScreen() {
        findNavController().navigate(R.id.action_loginFragment_to_homeScreenFragment)
    }

    private fun goToSignUp() {
        binding.txtSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

}