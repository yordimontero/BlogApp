package com.circleappsstudio.blogapp.domain.auth

import com.google.firebase.auth.FirebaseUser

interface AuthRepository {

    suspend fun signIn(email: String, password: String): FirebaseUser?

    suspend fun signUp(email: String, password: String, userName: String): FirebaseUser?

}