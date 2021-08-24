package com.circleappsstudio.blogapp.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.circleappsstudio.blogapp.R
import com.circleappsstudio.blogapp.core.Result
import com.circleappsstudio.blogapp.core.hide
import com.circleappsstudio.blogapp.core.show
import com.circleappsstudio.blogapp.core.toast
import com.circleappsstudio.blogapp.data.model.Post
import com.circleappsstudio.blogapp.data.remote.home.HomeScreenDataSource
import com.circleappsstudio.blogapp.databinding.FragmentHomeScreenBinding
import com.circleappsstudio.blogapp.domain.home.HomeScreenRepositoryImpl
import com.circleappsstudio.blogapp.presentation.HomeScreenViewModel
import com.circleappsstudio.blogapp.presentation.HomeScreenViewModelFactory
import com.circleappsstudio.blogapp.ui.home.adapter.HomeScreenAdapter
import com.circleappsstudio.blogapp.ui.home.adapter.onPostClickListener
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeScreenFragment : Fragment(R.layout.fragment_home_screen), onPostClickListener {

    private lateinit var binding: FragmentHomeScreenBinding

    private val viewModel by viewModels<HomeScreenViewModel> {
        HomeScreenViewModelFactory(
            HomeScreenRepositoryImpl(
                HomeScreenDataSource()
            )
        )
    }

    private val adapter = HomeScreenAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeScreenBinding.bind(view)

        binding.rvHome.adapter = adapter

        getLatestPostObserver()
        //getLatestPostMutableStateFlow()

    }

    private fun getLatestPostObserver() {

        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.latestPosts.collect { resultEmitted ->

                    when (resultEmitted) {

                        is Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is Result.Success -> {

                            if (resultEmitted.data.isEmpty()) {
                                binding.progressBar.visibility = View.GONE
                                binding.emptyContainer.show()
                                return@collect
                            } else {
                                binding.emptyContainer.hide()
                            }

                            adapter.setPostData(resultEmitted.data)

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

                }

            }

        }

    }

    private fun getLatestPostMutableStateFlow() {

        viewModel.fetchPosts()

        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.getPosts().collect { resultEmitted ->

                    when (resultEmitted) {

                        is Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is Result.Success -> {

                            if (resultEmitted.data.isEmpty()) {
                                binding.progressBar.visibility = View.GONE
                                binding.emptyContainer.show()
                                return@collect
                            } else {
                                binding.emptyContainer.hide()
                            }

                            adapter.setPostData(resultEmitted.data)

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

                }

            }

        }


    }

    override fun onLikeButtonClick(post: Post, liked: Boolean) {
        registerLikeButtonStateObserver(post.id, post.uid, liked)
    }

    private fun registerLikeButtonStateObserver(
        postId: String,
        uid: String,
        liked: Boolean
    ) {

        viewModel.registerLikeButtonState(postId, uid, liked)
            .observe(viewLifecycleOwner, Observer { resultEmitted ->
            when (resultEmitted) {
                is Result.Loading -> {
                }

                is Result.Success -> {

                    if (liked) {
                        requireContext().toast(requireContext(), "Post Liked!")
                    } else {
                        requireContext().toast(requireContext(), "Post Unliked!")
                    }

                }

                is Result.Failure -> {
                    Log.wtf("TAG", "Something went wrong: ${resultEmitted.exception}")
                }
            }
        })

    }

}