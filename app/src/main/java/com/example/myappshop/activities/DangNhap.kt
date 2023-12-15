package com.example.myappshop.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.View.OnClickListener
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.example.myappshop.R
import com.example.myappshop.databinding.ActivityDangnhapBinding
import com.google.firebase.auth.FirebaseAuth

class DangNhap : BaseActivity(), View.OnClickListener {
    private var binding: ActivityDangnhapBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDangnhapBinding.inflate(layoutInflater)
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
        binding?.signup?.setOnClickListener(this)
        binding?.signin?.setOnClickListener(this)
        binding?.getPass?.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        if (p0 != null) {
            when (p0.id) {
                binding?.getPass?.id -> {
                    startActivity(Intent(this@DangNhap,ForgotPasswordActivity::class.java))
                }

                binding?.signin?.id -> {
                    login()

                }

                binding?.signup?.id -> {
                    startActivity(Intent(this@DangNhap, DangKy::class.java))

                }

            }
        }
    }

    private fun validateLoginDetails(): Boolean {
        return when {
            TextUtils.isEmpty(binding?.etUserAccount?.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            TextUtils.isEmpty(binding?.etPassword?.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }

            else -> {
               // showErrorSnackBar("Your details are valid", false)
                true

            }
        }
    }

    private fun login() {
        if (validateLoginDetails()) {
            showProgressDialog(resources.getString(R.string.please_wait))

            val email: String = binding?.etUserAccount?.text.toString().trim { it <= ' ' }
            val passWord: String = binding?.etPassword?.text.toString().trim { it <= ' ' }

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, passWord)
                .addOnCompleteListener { task ->
                    run {
                        hideProgressDialog()
                        if (task!!.isSuccessful) {
                            showErrorSnackBar("You are logged in successfully", false)
                        } else {
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }

                    }
                }

        }
    }
}