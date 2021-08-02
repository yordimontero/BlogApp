package com.circleappsstudio.blogapp.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.circleappsstudio.blogapp.core.Result
import com.circleappsstudio.blogapp.domain.auth.AuthRepository
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    fun signIn(email: String, password: String) = liveData(
        viewModelScope.coroutineContext + Dispatchers.Main
    ) {

        emit(Result.Loading())

        try {

            emit(
                Result.Success(
                    repository.signIn(email, password)
                )
            )

        } catch (e: Exception) {
            emit(Result.Failure(e))
        }

    }

    fun signUp(email: String, password: String, userName: String) = liveData(
        viewModelScope.coroutineContext + Dispatchers.Main
    ) {

        emit(Result.Loading())

        try {

            emit(
                Result.Success(
                    repository.signUp(email, password, userName)
                )
            )

        } catch (e: Exception) {
            emit(Result.Failure(e))
        }

    }

}

class AuthViewModelFactory(private val repository: AuthRepository)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>)
            : T = AuthViewModel(repository) as T
}