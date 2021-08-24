package com.circleappsstudio.blogapp.domain.home

import com.circleappsstudio.blogapp.core.Result
import com.circleappsstudio.blogapp.data.model.Post
import kotlinx.coroutines.flow.Flow

interface HomeScreenRepository {
    suspend fun getLatestPosts(): List<Post>
    suspend fun registerLikeButtonState(
        postId: String,
        uid: String,
        liked: Boolean
    ): Boolean
}