package com.circleappsstudio.blogapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.circleappsstudio.blogapp.core.Result
import com.circleappsstudio.blogapp.domain.home.HomeScreenRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import java.lang.Exception

class HomeScreenViewModel(
    private val repository: HomeScreenRepository
) : ViewModel() {

    fun fetchLatestPosts() = liveData(
        viewModelScope.coroutineContext + Dispatchers.Main
    ) {

        emit(Result.Loading())

        kotlin.runCatching {

            repository.getLatestPosts()

        }.onSuccess { flowList ->

            flowList.collect { listPost ->
                emit(Result.Success(listPost))
            }

        }.onFailure { e ->
            emit(
                Result.Failure(
                    Exception(
                        e.message
                    )
                )
            )
        }

    }

}

class HomeScreenViewModelFactory(
    private val repository: HomeScreenRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>)
            : T = HomeScreenViewModel(repository) as T

}