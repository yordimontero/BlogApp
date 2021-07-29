package com.circleappsstudio.blogapp.domain.home

import com.circleappsstudio.blogapp.core.Resource
import com.circleappsstudio.blogapp.data.model.Post

interface HomeScreenRepository {
    suspend fun getLatestPosts(): Resource<List<Post>>
}