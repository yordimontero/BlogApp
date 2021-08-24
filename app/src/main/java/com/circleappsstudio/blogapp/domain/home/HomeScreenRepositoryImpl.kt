package com.circleappsstudio.blogapp.domain.home

import com.circleappsstudio.blogapp.core.Result
import com.circleappsstudio.blogapp.data.model.Post
import com.circleappsstudio.blogapp.data.remote.home.HomeScreenDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class HomeScreenRepositoryImpl(
    private val dataSource: HomeScreenDataSource
) : HomeScreenRepository {

    override suspend fun getLatestPosts()
            : List<Post> = withContext(Dispatchers.IO) {
        dataSource.getLatestPosts()
    }

    override suspend fun registerLikeButtonState(
        postId: String,
        uid: String,
        liked: Boolean
    ): Boolean = withContext(Dispatchers.IO) {
        dataSource.registerLikeButtonState(postId, uid, liked)
    }

}