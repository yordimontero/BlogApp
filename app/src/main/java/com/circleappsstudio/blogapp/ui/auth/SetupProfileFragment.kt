package com.circleappsstudio.blogapp.ui.auth

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.circleappsstudio.blogapp.R
import com.circleappsstudio.blogapp.core.Result
import com.circleappsstudio.blogapp.core.hide
import com.circleappsstudio.blogapp.core.show
import com.circleappsstudio.blogapp.core.toast
import com.circleappsstudio.blogapp.data.remote.auth.AuthDataSource
import com.circleappsstudio.blogapp.databinding.FragmentSetupProfileBinding
import com.circleappsstudio.blogapp.domain.auth.AuthRepositoryImpl
import com.circleappsstudio.blogapp.presentation.auth.AuthViewModel
import com.circleappsstudio.blogapp.presentation.auth.AuthViewModelFactory

class SetupProfileFragment : Fragment(R.layout.fragment_setup_profile) {

    private lateinit var binding: FragmentSetupProfileBinding

    private val IMAGE_REQUEST_CAPTURE = 1

    private var bitmap: Bitmap? = null

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

        takePicture()

        createProfile()

    }

    private fun takePicture() {

        binding.profileImage.setOnClickListener {

            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            try {

                startActivityForResult(takePictureIntent, IMAGE_REQUEST_CAPTURE)

            } catch (e: ActivityNotFoundException) {
                requireContext().toast(requireContext(), "Camera not founded!")
            }

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_REQUEST_CAPTURE && resultCode == Activity.RESULT_OK) {

            val imageBitmap = data?.extras?.get("data") as Bitmap

            binding.profileImage.setImageBitmap(imageBitmap)

            bitmap = imageBitmap

        }

    }

    private fun createProfile() {

        binding.btnCreateProfile.setOnClickListener {

            val userName = binding.txtUserName.text.toString().trim()

            checkData(bitmap, userName)

        }

    }

    private fun checkData(bitmap: Bitmap?, userName: String) {

        if (bitmap == null) {
            return
        }

        if (userName.isEmpty()) {
            binding.txtUserName.error = "Empty field!"
            return
        }

        updateUserProfileObserver(bitmap, userName)

    }

    private fun updateUserProfileObserver(bitmap: Bitmap, userName: String) {

        viewModel.updateUserProfile(bitmap, userName)
            .observe(viewLifecycleOwner, Observer { resultEmitted ->

                when(resultEmitted) {

                    is Result.Loading -> {
                        binding.progressBar.show()
                    }

                    is Result.Success -> {

                        findNavController().navigate(R.id.action_setupProfileFragment_to_homeScreenFragment)
                        binding.progressBar.hide()

                    }

                    is Result.Failure -> {

                        requireContext().toast(
                            requireContext(),
                            "Something went wrong: ${resultEmitted.exception.message}",
                            Toast.LENGTH_LONG
                        )

                        binding.progressBar.hide()

                    }

                }

            })


    }

}