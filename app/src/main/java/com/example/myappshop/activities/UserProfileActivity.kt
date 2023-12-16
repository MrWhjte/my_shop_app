package com.example.myappshop.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.View.OnClickListener
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myappshop.R
import com.example.myappshop.databinding.ActivityUserProfileBinding
import com.example.myappshop.firestore.FirestoreClass
import com.example.myappshop.models.User
import com.example.myappshop.utils.Constants
import com.example.myappshop.utils.GliderLoader
import java.io.IOException

class UserProfileActivity : BaseActivity(), OnClickListener {
    private var binding: ActivityUserProfileBinding? = null
    private lateinit var mUserInfo: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController!!.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)) {
            mUserInfo = intent.getParcelableExtra<User>(Constants.EXTRA_USER_DETAILS)!!
        }
        binding?.etFirstName?.isEnabled = false
        binding?.etFirstName?.setText(mUserInfo.firstName)

        binding?.etLastName?.isEnabled = false
        binding?.etLastName?.setText(mUserInfo.lastName)

        binding?.etEmail?.isEnabled = false
        binding?.etEmail?.setText(mUserInfo.emaiAdd)


        binding?.btnSubmit?.setOnClickListener(this@UserProfileActivity)
        binding?.ivUserPhoto?.setOnClickListener(this@UserProfileActivity)
    }

    override fun onClick(p0: View?) {
        if (p0 != null) {
            when (p0.id) {
                binding?.ivUserPhoto?.id -> {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        //showErrorSnackBar("You already have the storage permission", false)
                        Constants.showImageChooser(this)
                    } else {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }

                binding?.btnSubmit?.id -> {
                    if (validateUserProfileDetail()) {
                        //showErrorSnackBar("Your details are valid. You can update them",false)
                        val userHashMap = HashMap<String, Any>()

                        val mobilePhone =
                            binding?.etMobileNumber?.text.toString().trim { it <= ' ' }
                        val birthday = binding?.etBirthday?.text.toString().trim { it <= ' ' }
                        val address = binding?.etAdd?.text.toString().trim { it <= ' ' }

                        val gender = if (binding?.rbMale!!.isChecked) {
                            Constants.MALE
                        } else {
                            Constants.FEMALE
                        }


                        if (mobilePhone.isNotEmpty()) {
                            userHashMap[Constants.MOBILE] = mobilePhone.toLong()
                        }
                        userHashMap[Constants.GENDER] = gender
                        userHashMap[Constants.BIRTHDAY] = birthday
                        userHashMap[Constants.ADDRESS] = address

                        showProgressDialog(resources.getString(R.string.please_wait))
                        FirestoreClass().updateUserProfileData(this, userHashMap)
                    }
                }
            }
        }
    }

    fun userProfileUpdateSuccess() {
        hideProgressDialog()

        Toast.makeText(
            this@UserProfileActivity,
            resources.getString(R.string.msg_profile_update_success),
            Toast.LENGTH_LONG
        ).show()
        startActivity(Intent(this@UserProfileActivity,MainActivity::class.java))
        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //showErrorSnackBar("The storage permission is granted.",false)
                Constants.showImageChooser(this)

            } else {
                Toast.makeText(
                    this,
                    resources.getString(R.string.read_storage_permission_denied),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE) {
                if (data != null) {
                    try {
                        val selectedImageFileUri = data.data!!
                        //binding?.ivUserPhoto?.setImageURI(selectedImageFileUri)
                        GliderLoader(this).loadUserPicture(
                            selectedImageFileUri,
                            binding?.ivUserPhoto!!
                        )
                        showErrorSnackBar("Success", false)

                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this,
                            resources.getString(R.string.image_selection_failed),
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }
            }
        }
    }

    fun validateUserProfileDetail(): Boolean {
        return when {
            TextUtils.isEmpty(binding?.etMobileNumber?.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_phone_number), true)
                false
            }

            TextUtils.isEmpty(binding?.etBirthday?.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_enter_birthday), true
                )
                false
            }

            TextUtils.isEmpty(binding?.etAdd?.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_address), true)
                false
            }

            else -> {
                true
            }
        }

    }

}

