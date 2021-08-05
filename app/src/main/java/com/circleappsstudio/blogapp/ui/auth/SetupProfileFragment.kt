package com.circleappsstudio.blogapp.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import com.circleappsstudio.blogapp.R
import com.circleappsstudio.blogapp.data.remote.auth.AuthDataSource
import com.circleappsstudio.blogapp.databinding.FragmentSetupProfileBinding
import com.circleappsstudio.blogapp.domain.auth.AuthRepositoryImpl
import com.circleappsstudio.blogapp.presentation.auth.AuthViewModel
import com.circleappsstudio.blogapp.presentation.auth.AuthViewModelFactory

class SetupProfileFragment : Fragment(R.layout.fragment_setup_profile) {

    private lateinit var binding: FragmentSetupProfileBinding

    private val viewModel by viewModels<AuthViewModel> {
        AuthViewModelFactory(
            AuthRepositoryImpl(
                AuthDataSource()
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSetupProfileBinding.bind(view)

    }

    /*private fun updateUserProfileObserver() {

        viewModel.updateUserProfile()

    }*/

}