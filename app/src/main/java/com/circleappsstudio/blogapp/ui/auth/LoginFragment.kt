package com.circleappsstudio.blogapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.util.PatternsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.circleappsstudio.blogapp.R
import com.circleappsstudio.blogapp.core.Result
import com.circleappsstudio.blogapp.core.toast
import com.circleappsstudio.blogapp.data.remote.auth.AuthDataSource
import com.circleappsstudio.blogapp.databinding.FragmentLoginBinding
import com.circleappsstudio.blogapp.domain.auth.AuthRepositoryImpl
import com.circleappsstudio.blogapp.presentation.auth.AuthViewModel
import com.circleappsstudio.blogapp.presentation.auth.AuthViewModelFactory
import com.circleappsstudio.blogapp.ui.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val viewModel by viewModels<AuthViewModel> {
        AuthViewModelFactory(
            AuthRepositoryImpl(
                AuthDataSource()
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
        firebaseAuth.currentUser?.let { user ->
            validateUserProfile(user)
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

                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        validateUserProfile(resultEmitted.data)
                        binding.progressBar.visibility = View.GONE
                    }

                    is Result.Failure -> {

                        requireContext().toast(
                            requireContext(),
                            "Something went wrong: ${resultEmitted.exception.message}",
                            Toast.LENGTH_LONG
                        )

                        binding.progressBar.visibility = View.GONE

                    }

                }

            })

    }

    private fun validateUserProfile(user: FirebaseUser?) {

        if (user?.displayName.isNullOrEmpty() || user?.photoUrl == null) {
            goToSetupProfile()
        } else {
            goToHomeScreen()
        }

    }

    private fun goToHomeScreen() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun goToSetupProfile() {
        findNavController().navigate(R.id.action_loginFragment_to_setupProfileFragment)
    }

    private fun goToSignUp() {
        binding.txtSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

}