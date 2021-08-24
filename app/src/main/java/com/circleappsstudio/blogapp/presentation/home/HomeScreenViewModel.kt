package com.circleappsstudio.blogapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.circleappsstudio.blogapp.core.Result
import com.circleappsstudio.blogapp.data.model.Post
import com.circleappsstudio.blogapp.domain.home.HomeScreenRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception

class HomeScreenViewModel(
    private val repository: HomeScreenRepository
) : ViewModel() {

    val latestPosts: StateFlow<Result<List<Post>>> = flow {

        kotlin.runCatching {

            repository.getLatestPosts()

        }.onSuccess { resultPostList ->

            emit(Result.Success(resultPostList))

        }.onFailure { throwable ->

            emit(
                Result.Failure(
                    Exception(
                        throwable
                    )
                )
            )

        }

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = Result.Loading()
    )

    private val posts = MutableStateFlow<Result<List<Post>>>(Result.Loading())

    fun fetchPosts() = viewModelScope.launch {

        kotlin.runCatching {

            repository.getLatestPosts()

        }.onSuccess { resultPostList ->

            posts.value = Result.Success(resultPostList)

        }.onFailure { throwable ->
            posts.value = Result.Failure(
                Exception(
                    throwable
                )
            )
        }

    }

    fun getPosts(): StateFlow<Result<List<Post>>> = posts

    fun registerLikeButtonState(
        postId: String,
        uid: String,
        liked: Boolean
    ) = liveData(viewModelScope.coroutineContext + Dispatchers.Main) {

        emit(Result.Loading())

        kotlin.runCatching {
            repository.registerLikeButtonState(postId, uid, liked)
        }.onSuccess { likeButtonState ->
            emit(Result.Success(likeButtonState))
        }.onFailure { throwable ->
            emit(Result.Failure(Exception(throwable.message)))
        }

    }

}

class HomeScreenViewModelFactory(
    private val repository: HomeScreenRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>)
            : T = HomeScreenViewModel(repository) as T

}