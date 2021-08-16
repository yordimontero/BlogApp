package com.circleappsstudio.blogapp.presentation.camera

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.circleappsstudio.blogapp.core.Result
import com.circleappsstudio.blogapp.domain.camera.CameraRepository
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class CameraViewModel(
    private val repository: CameraRepository
) : ViewModel() {

    fun uploadPhoto(
        imageBitmap: Bitmap,
        description: String
    ) = liveData(viewModelScope.coroutineContext + Dispatchers.Main) {

        emit(Result.Loading())

        try {

            emit(
                Result.Success(
                    repository.uploadPhoto(
                        imageBitmap,
                        description
                    )
                )
            )

        } catch (e: Exception) {
            emit(Result.Failure(e))
        }

    }

}

class CameraViewModelFactory(
    private val repository: CameraRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>)
    : T = CameraViewModel(repository) as T
}