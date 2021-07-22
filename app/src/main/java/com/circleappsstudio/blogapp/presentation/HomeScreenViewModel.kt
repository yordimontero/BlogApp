package com.circleappsstudio.blogapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.circleappsstudio.blogapp.core.Resource
import com.circleappsstudio.blogapp.domain.HomeScreenRepository
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class HomeScreenViewModel(
    private val repository: HomeScreenRepository
) : ViewModel() {

    fun fetchLatestPosts() = liveData(viewModelScope.coroutineContext + Dispatchers.Main) {

        emit(Resource.Loading())

        try {
            emit(repository.getLatestPosts())
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }

    }

}

class HomeScreenViewModelFactory(
    private val repository: HomeScreenRepository
): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>)
    : T = modelClass.getConstructor(HomeScreenRepository::class.java).newInstance(repository)

}