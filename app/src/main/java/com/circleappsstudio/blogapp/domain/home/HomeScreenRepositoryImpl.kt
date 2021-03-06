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

    @ExperimentalCoroutinesApi
    override suspend fun getLatestPosts()
    : Flow<List<Post>> = withContext(Dispatchers.IO) {
        dataSource.getLatestPosts()
    }

}