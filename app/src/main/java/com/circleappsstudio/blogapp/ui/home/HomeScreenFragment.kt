package com.circleappsstudio.blogapp.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.circleappsstudio.blogapp.R
import com.circleappsstudio.blogapp.core.Result
import com.circleappsstudio.blogapp.data.remote.home.HomeScreenDataSource
import com.circleappsstudio.blogapp.databinding.FragmentHomeScreenBinding
import com.circleappsstudio.blogapp.domain.home.HomeScreenRepositoryImpl
import com.circleappsstudio.blogapp.presentation.HomeScreenViewModel
import com.circleappsstudio.blogapp.presentation.HomeScreenViewModelFactory
import com.circleappsstudio.blogapp.ui.home.adapter.HomeScreenAdapter

class HomeScreenFragment : Fragment(R.layout.fragment_home_screen) {

    private lateinit var binding: FragmentHomeScreenBinding
    private val viewModel by viewModels<HomeScreenViewModel> {
        HomeScreenViewModelFactory(
            HomeScreenRepositoryImpl(
                HomeScreenDataSource()
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeScreenBinding.bind(view)

        getLatestPostObserver()

    }

    private fun getLatestPostObserver() {

        viewModel.fetchLatestPosts()
            .observe(
                viewLifecycleOwner, Observer { resultEmitted ->

                    when (resultEmitted) {

                        is Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is Result.Success -> {
                            binding.rvHome.adapter = HomeScreenAdapter(
                                resultEmitted.data
                            )
                            binding.progressBar.visibility = View.GONE
                        }

                        is Result.Failure -> {
                            Toast.makeText(
                                requireContext(),
                                "Something went wrong: ${resultEmitted.exception.message}",
                                Toast.LENGTH_LONG
                            ).show()
                            binding.progressBar.visibility = View.GONE
                        }

                    }

                }

            )

    }

}