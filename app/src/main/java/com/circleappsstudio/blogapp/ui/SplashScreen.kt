package com.circleappsstudio.blogapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.circleappsstudio.blogapp.R
import com.circleappsstudio.blogapp.ui.auth.AuthActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SplashScreen : AppCompatActivity() {

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        isUserLoggedIn()

    }

    private fun isUserLoggedIn() {

        if (firebaseAuth.currentUser == null) {
            goToAuthActivity()
        } else {
            validateUserProfile(firebaseAuth.currentUser)
        }

    }

    private fun validateUserProfile(user: FirebaseUser?) {

        if (user?.displayName.isNullOrEmpty() || user?.photoUrl == null) {
            goToAuthActivity()
        } else {
            goToHomeScreen()
        }

    }

    private fun goToHomeScreen() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun goToAuthActivity() {
        val intent = Intent(this, AuthActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

}