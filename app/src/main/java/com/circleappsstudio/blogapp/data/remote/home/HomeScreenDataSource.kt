package com.circleappsstudio.blogapp.data.remote.home

import com.circleappsstudio.blogapp.data.model.Post
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class HomeScreenDataSource {

    suspend fun getLatestPosts(): List<Post> {

        val postList = mutableListOf<Post>()

        val querySnapshot = FirebaseFirestore
            .getInstance()
            .collection("posts")
            .get()
            .await()

        for (post in querySnapshot.documents) {
            post.toObject(Post::class.java)?.let { firebasePost ->

                firebasePost.apply {
                    created_at = post.getTimestamp("created_at", DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)?.toDate()
                }

                postList.add(firebasePost)
                
            }
        }

        return postList
    }

}