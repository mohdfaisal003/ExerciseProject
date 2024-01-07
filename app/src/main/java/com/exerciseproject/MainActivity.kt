package com.exerciseproject

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.exerciseproject.databinding.ActivityMainBinding
import com.test.project.mvvm.AuthViewModel

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    val authViewModel by viewModels<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authViewModel.authResult.observe(this, Observer {
            if (it.isSuccess) {
                Toast.makeText(this, "${it.message} ${it.otp}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        })

        binding.btnSendOTP.setOnClickListener {
            if (binding.mobileNumberEt.text.toString().isNotEmpty()) {
                authViewModel.sendVerificationCode(this, binding.mobileNumberEt.text.toString())
            } else {
                Toast.makeText(this, "Please enter a valid mobile number", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}