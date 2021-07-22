package com.circleappsstudio.blogapp.domain

import com.circleappsstudio.blogapp.core.Resource
import com.circleappsstudio.blogapp.data.model.Post
import com.circleappsstudio.blogapp.data.remote.HomeScreenDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HomeScreenRepositoryImpl(
    private val dataSource: HomeScreenDataSource
) : HomeScreenRepository {

    override suspend fun getLatestPosts()
    : Resource<List<Post>> = withContext(Dispatchers.IO) {
        dataSource.getLatestPosts()
    }

}