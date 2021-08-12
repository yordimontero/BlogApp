package com.circleappsstudio.blogapp.data.remote.auth

import android.graphics.Bitmap
import android.net.Uri
import com.circleappsstudio.blogapp.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream

class AuthDataSource {

    suspend fun signIn(email: String, password: String): FirebaseUser? {

        val authResult = FirebaseAuth
            .getInstance()
            .signInWithEmailAndPassword(email, password)
            .await()

        return authResult.user

    }

    suspend fun signUp(email: String, password: String, userName: String): FirebaseUser? {

        val authResult = FirebaseAuth
            .getInstance()
            .createUserWithEmailAndPassword(email, password)
            .await()

        authResult.user?.uid?.let { uid ->
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .set(
                    User(
                        email,
                        userName,
                        "Photo_Url.jpg"
                    )
                ).await()
        }

        return authResult.user

    }

    suspend fun updateUserProfile(imageBitmap: Bitmap, userName: String) {

        val user = FirebaseAuth.getInstance().currentUser

        val imageRef = FirebaseStorage
            .getInstance()
            .reference
            .child("${user?.uid}/profile_picture")

        val baos = ByteArrayOutputStream()

        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)

        val downloadURL = imageRef
            .putBytes(
                baos.toByteArray()
            ).await()
            .storage
            .downloadUrl.await().toString()

        val profileUpdates = userProfileChangeRequest {
            displayName = userName
            photoUri = Uri.parse(downloadURL)
        }

        user?.updateProfile(profileUpdates)?.await()


    }

}