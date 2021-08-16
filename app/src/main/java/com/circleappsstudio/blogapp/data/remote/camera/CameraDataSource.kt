package com.circleappsstudio.blogapp.data.remote.camera

import android.graphics.Bitmap
import com.circleappsstudio.blogapp.data.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.*

class CameraDataSource {

    suspend fun uploadPhoto(
        imageBitmap: Bitmap,
        description: String
    ) {

        val user = FirebaseAuth.getInstance().currentUser

        val randomName = UUID.randomUUID().toString()

        val baos = ByteArrayOutputStream()

        val imageRef = FirebaseStorage
            .getInstance()
            .reference
            .child("users/${user?.uid}/posts/$randomName")

        imageBitmap.compress(
            Bitmap.CompressFormat.JPEG,
            100,
            baos
        )

        val downloadURL = imageRef
            .putBytes(
                baos.toByteArray()
            ).await()
            .storage.downloadUrl.await().toString()

        user?.let { firebaseUser ->

            FirebaseFirestore
                .getInstance()
                .collection("posts")
                .add(
                    Post(
                        profile_picture = firebaseUser.photoUrl.toString(),
                        profile_name = firebaseUser.displayName.toString(),
                        post_image = downloadURL,
                        post_description = description,
                        uid = firebaseUser.uid
                    )
                ).await()

        }

    }

}