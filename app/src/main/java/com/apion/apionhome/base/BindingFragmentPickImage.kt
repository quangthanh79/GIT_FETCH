package com.apion.apionhome.base

import android.app.Activity
import androidx.activity.result.ActivityResult
import androidx.viewbinding.ViewBinding
import com.apion.apionhome.utils.FileUtils
import com.apion.apionhome.utils.getRealPath
import com.github.dhaval2404.imagepicker.ImagePicker

abstract class BindingFragmentPickImage<T : ViewBinding>
    (inflate: Inflate<T>) : BindingFragment<T>(inflate) {

    override fun onPermissionResult(permissions: MutableMap<String, Boolean>) {
        super.onPermissionResult(permissions)
        if (hasPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            pickImage()
        }
    }

    override fun onActivityResult(result: ActivityResult) {
        super.onActivityResult(result)
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data!!
            val path = FileUtils.getReadablePathFromUri(requireContext(), uri)
            onImagesSelect(path?:"")
        }
    }

    abstract fun onImagesSelect(path: String)

    fun pickImageSafety() {
        if (hasPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            pickImage()
        } else {
            requestPermissionsSafely(arrayListOf(android.Manifest.permission.READ_EXTERNAL_STORAGE))
        }
    }

    private fun pickImage() {
        ImagePicker.with(this)
            .galleryOnly()
            .compress(1024)         //Final image size will be less than 1 MB(Optional)
            .maxResultSize(
                1080,
                1080
            )  //Final image resolution will be less than 1080 x 1080(Optional)
            .createIntent { intent ->
                startActivityForResultSafely(intent)
            }
    }
}
