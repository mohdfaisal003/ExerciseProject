package com.exerciseproject.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.exerciseproject.databinding.ActivityMainBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.test.project.mvvm.AuthViewModel


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    val authViewModel by viewModels<AuthViewModel>()
    var verificationId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authViewModel.authResult.observe(this, Observer {
            if (it) {

                val intent = Intent(this, DashboardActivity::class.java)
                intent.putExtra("verificationId", verificationId)
                startActivity(intent)
                Toast.makeText(this, "Successfully Authenticated", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Auth Failed", Toast.LENGTH_SHORT).show()
            }
        })

        binding.idBtnGetOtp.setOnClickListener {
            if (binding.idEdtPhoneNumber.text.isNotEmpty()) {
                authViewModel.sendVerificationCode(
                    this,
                    binding.idEdtPhoneNumber.text.toString(),
                    phoneAuthCallBack
                )
            } else {
                Toast.makeText(this, "Please enter a valid mobile number", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.idBtnVerify.setOnClickListener {
            if (binding.idEdtOtp.text.isNotEmpty()) {
                authViewModel.verifyCode(
                    this,
                    verificationId.toString(),
                    binding.idEdtOtp.text.toString()
                )
            }
        }
    }


    private val phoneAuthCallBack: OnVerificationStateChangedCallbacks =
        object : OnVerificationStateChangedCallbacks() {

            override fun onCodeSent(id: String, forceResendingToken: ForceResendingToken) {
                super.onCodeSent(id, forceResendingToken)
                Log.d("onCodeSent", id)
                verificationId = id
            }

            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                Log.d("onVerificationCompleted", phoneAuthCredential.smsCode.toString())
                val otp = phoneAuthCredential.smsCode
                if (otp != null) {
                    binding.idEdtOtp.setText(otp)
                }
            }

            override fun onVerificationFailed(exception: FirebaseException) {
                Log.e("onVerificationFailed", exception.message.toString())
                Toast.makeText(this@MainActivity, exception.message, Toast.LENGTH_LONG).show()
            }
        }
}