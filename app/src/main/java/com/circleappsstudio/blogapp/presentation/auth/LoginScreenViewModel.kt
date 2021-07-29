package com.circleappsstudio.blogapp.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.circleappsstudio.blogapp.core.Resource
import com.circleappsstudio.blogapp.domain.auth.LoginRepository
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class LoginScreenViewModel(private val repository: LoginRepository) : ViewModel() {

    fun signIn(email: String, password: String) = liveData(
        viewModelScope.coroutineContext + Dispatchers.Main
    ) {

        emit(Resource.Loading())

        try {

            emit(
                Resource.Success(
                    repository.signIn(email, password)
                )
            )

        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }

    }

}

class LoginScreenViewModelFactory(private val repository: LoginRepository)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>)
            : T = LoginScreenViewModel(repository) as T
}