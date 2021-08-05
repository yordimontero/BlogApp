package com.circleappsstudio.blogapp.domain.auth

import android.graphics.Bitmap
import com.circleappsstudio.blogapp.data.remote.auth.AuthDataSource
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepositoryImpl(private val dataSource: AuthDataSource) : AuthRepository {

    override suspend fun signIn(
        email: String,
        password: String
    ): FirebaseUser? = withContext(Dispatchers.IO) {
        dataSource.signIn(email, password)
    }

    override suspend fun signUp(
        email: String,
        password: String,
        userName: String
    ): FirebaseUser? = withContext(Dispatchers.IO) {
        dataSource.signUp(email, password, userName)
    }

    override suspend fun updateUserProfile(
        imageBitmap: Bitmap,
        userName: String
    ) = withContext(Dispatchers.IO) {
        dataSource.updateUserProfile(imageBitmap, userName)
    }

}