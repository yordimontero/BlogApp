package com.circleappsstudio.blogapp.domain.home

import com.circleappsstudio.blogapp.core.Result
import com.circleappsstudio.blogapp.data.model.Post

interface HomeScreenRepository {
    suspend fun getLatestPosts(): Result<List<Post>>
}