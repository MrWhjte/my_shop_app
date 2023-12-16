package com.example.myappshop.utils

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore

object Constants {
    const val USERS: String = "users"
    const val MYSHOPAPP_PREFERENCES = "MyShopAppPrefs"
    const val LOGGED_IN_USERNAME = "logged_in_username"
    const val EXTRA_USER_DETAILS: String = "extra_user_detail"

    const val READ_STORAGE_PERMISSION_CODE = 2
    const val PICK_IMAGE_REQUEST_CODE = 1

    const val MALE:String ="Male"
    const val FEMALE:String ="Female"

    const val MOBILE:String = "mobile"
    const val GENDER:String  = "gender"
    const val BIRTHDAY:String = "birthDay"
    const val ADDRESS:String = "address"

    fun showImageChooser(activity: Activity) {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }
}