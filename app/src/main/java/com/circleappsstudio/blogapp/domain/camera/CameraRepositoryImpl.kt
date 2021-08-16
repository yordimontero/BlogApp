package com.circleappsstudio.blogapp.domain.camera

import android.graphics.Bitmap
import com.circleappsstudio.blogapp.data.remote.camera.CameraDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CameraRepositoryImpl(
    private val dataSource: CameraDataSource
) : CameraRepository {

    override suspend fun uploadPhoto(
        imageBitmap: Bitmap,
        description: String
    ) = withContext(Dispatchers.IO) {
        dataSource.uploadPhoto(imageBitmap, description)
    }

}