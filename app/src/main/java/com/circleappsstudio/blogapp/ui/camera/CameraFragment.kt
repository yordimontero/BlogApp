package com.circleappsstudio.blogapp.ui.camera

import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.circleappsstudio.blogapp.R
import com.circleappsstudio.blogapp.core.Result
import com.circleappsstudio.blogapp.core.hide
import com.circleappsstudio.blogapp.core.show
import com.circleappsstudio.blogapp.core.toast
import com.circleappsstudio.blogapp.data.remote.camera.CameraDataSource
import com.circleappsstudio.blogapp.databinding.FragmentCameraBinding
import com.circleappsstudio.blogapp.domain.camera.CameraRepositoryImpl
import com.circleappsstudio.blogapp.presentation.camera.CameraViewModel
import com.circleappsstudio.blogapp.presentation.camera.CameraViewModelFactory

class CameraFragment : Fragment(R.layout.fragment_camera) {

    private lateinit var binding: FragmentCameraBinding

    private val viewModel by viewModels<CameraViewModel> {
        CameraViewModelFactory(
            CameraRepositoryImpl(
                CameraDataSource()
            )
        )
    }

    private var bitmap: Bitmap? = null

    private val IMAGE_REQUEST_CAPTURE = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCameraBinding.bind(view)

        takePicture()

        uploadPost()

    }

    private fun takePicture() {

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        try {
            startActivityForResult(takePictureIntent, IMAGE_REQUEST_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            requireContext().toast(requireContext(), "Camera not founded!")
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_REQUEST_CAPTURE && resultCode == RESULT_OK) {

            val imageBitmap = data?.extras?.get("data") as Bitmap

            bitmap = imageBitmap

            binding.imgPostImage.setImageBitmap(imageBitmap)

        }

    }

    private fun uploadPost() {

        binding.btnUploadPost.setOnClickListener {

            val imageBitmap = bitmap
            val postDescription = binding.txtPostDescription.text.toString().trim()

            validatePost(imageBitmap, postDescription)

        }

    }

    private fun validatePost(imageBitmap: Bitmap?, postDescription: String) {

        if (imageBitmap == null) {
            requireContext().toast(requireContext(), "Post image empty!")
            return
        }

        if (postDescription.isEmpty()) {
            binding.txtPostDescription.error = "Empty description!"
            return
        }

        uploadPhotoObserver(imageBitmap, postDescription)

    }

    private fun uploadPhotoObserver(bitmap: Bitmap, description: String) {

        viewModel.uploadPhoto(bitmap, description)
            .observe(viewLifecycleOwner, Observer { resultEmitted ->

                when(resultEmitted) {

                    is Result.Loading -> {
                        binding.progressBar.show()
                    }

                    is Result.Success -> {
                        binding.progressBar.hide()
                        goToHomeScreenFragment()
                    }

                    is Result.Failure -> {

                        requireContext().toast(
                            requireContext(),
                            "Something went wrong: ${resultEmitted.exception.message}"
                        )
                        binding.progressBar.hide()

                    }

                }

            })

    }

    private fun goToHomeScreenFragment() {
        findNavController().navigate(R.id.action_cameraFragment_to_homeScreenFragment)
    }

}