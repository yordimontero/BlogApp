package com.circleappsstudio.blogapp.data.remote.home

import com.circleappsstudio.blogapp.data.model.Post
import com.google.firebase.firestore.*
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

                            id = post.id

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

    // Likes Testing:
    suspend fun registerLikeButtonState(postId: String, uid: String, liked: Boolean): Boolean {

        val increment = FieldValue.increment(1)
        val decrement = FieldValue.increment(-1)

        val postRef = FirebaseFirestore.getInstance().collection("posts").document(postId)
        val postsLikesRef =
            FirebaseFirestore.getInstance().collection("posts_likes").document(postId)

        val batch = FirebaseFirestore.getInstance().batch()

        if (liked) {
            batch.set(postRef, hashMapOf("likes" to increment), SetOptions.merge())
            batch.set(postsLikesRef, hashMapOf(uid to true), SetOptions.merge())
        } else {
            batch.set(postRef, hashMapOf("likes" to decrement), SetOptions.merge())
            batch.delete(postsLikesRef)
        }

        batch.commit().await()

        return liked
    }

}