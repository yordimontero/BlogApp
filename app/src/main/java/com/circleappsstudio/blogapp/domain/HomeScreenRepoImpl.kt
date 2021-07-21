package com.circleappsstudio.blogapp.domain

import com.circleappsstudio.blogapp.core.Resource
import com.circleappsstudio.blogapp.data.model.Post
import com.circleappsstudio.blogapp.data.remote.HomeScreenDataSource

class HomeScreenRepoImpl(private val dataSource: HomeScreenDataSource) : HomeScreenRepo {

    override suspend fun getLatestPosts()
    : Resource<List<Post>> = dataSource.getLatestPosts()

}