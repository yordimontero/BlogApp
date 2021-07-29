package com.circleappsstudio.blogapp.domain.auth

import com.circleappsstudio.blogapp.data.remote.auth.LoginDataSource
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginRepositoryImpl(private val dataSource: LoginDataSource) : LoginRepository {

    override suspend fun signIn(
        email: String,
        password: String
    ): FirebaseUser? = withContext(Dispatchers.IO) {
        dataSource.signIn(email, password)
    }

}