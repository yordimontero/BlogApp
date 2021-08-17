package com.circleappsstudio.blogapp.data.remote.home

import com.circleappsstudio.blogapp.data.model.Post
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class HomeScreenDataSource {

    @ExperimentalCoroutinesApi
    suspend fun getLatestPosts(): Flow<List<Post>> = callbackFlow {

        val postList = mutableListOf<Post>()

        var postReference: Query? = null

        try {

            postReference = FirebaseFirestore
                .getInstance()
                .collection("posts")
                .orderBy("created_at", Query.Direction.DESCENDING)

        } catch (e: Throwable) {
            close(e)
        }

        val subscription = postReference?.addSnapshotListener { value, error ->

            if (value == null) return@addSnapshotListener

            try {

                postList.clear()

                for (post in value.documents) {
                    post.toObject(Post::class.java)?.let { firebasePost ->

                        firebasePost.apply {
                            created_at = post.getTimestamp(
                                "created_at",
                                DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                            )?.toDate()
                        }

                        postList.add(firebasePost)

                    }

                }

            } catch (e: Exception) {
                close(e)
            }

            offer(postList)

        }

        awaitClose {
            subscription?.remove()
        }

    }

}