package com.circleappsstudio.blogapp.data.remote.home

import com.circleappsstudio.blogapp.core.Resource
import com.circleappsstudio.blogapp.data.model.Post
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class HomeScreenDataSource {

    suspend fun getLatestPosts(): Resource<List<Post>> {

        val postList = mutableListOf<Post>()

        val querySnapshot = FirebaseFirestore
            .getInstance()
            .collection("posts")
            .get()
            .await()

        for (post in querySnapshot.documents) {
            post.toObject(Post::class.java)?.let { firebasePost ->
                postList.add(firebasePost)
            }
        }

        return Resource.Success(postList)
    }

}