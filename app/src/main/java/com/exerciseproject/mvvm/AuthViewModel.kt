package com.test.project.mvvm

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AuthViewModel : ViewModel() {

    var authResult = MutableLiveData<Boolean>()
    private val auth = FirebaseAuth.getInstance()

    fun sendVerificationCode(activity: Activity, phoneNumber: String, callBack: OnVerificationStateChangedCallbacks) {
        Log.d("sendVerificationCodePhoneNumber","+91${phoneNumber}")
        viewModelScope.launch {
            try {
                val options = PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber("+91${phoneNumber}")
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setActivity(activity)
                    .setCallbacks(callBack)
                    .build()
                PhoneAuthProvider.verifyPhoneNumber(options)
                Toast.makeText(activity,"OTP Sent to ${phoneNumber}",Toast.LENGTH_SHORT).show()
            } catch (exception: Exception) {
                exception.printStackTrace()
                Log.e("PhoneAuthException",exception.message.toString())
            }
        }
    }

    fun verifyCode(activity: Activity,verificationId: String,otp: String) {
        Log.d("verifyCode",otp)
        val credential = PhoneAuthProvider.getCredential(verificationId, otp)

        signInWithCredential(activity,credential)
    }

    private fun signInWithCredential(activity: Activity,credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(OnCompleteListener<AuthResult?> { task ->
                if (task.isSuccessful) {
                    authResult.value = true
                } else {
                    authResult.value = false
                    Toast.makeText(activity, task.exception!!.message, Toast.LENGTH_LONG)
                        .show()
                }
            })
    }

//    fun sendVerificationCode(activity: Activity, phoneNumber: String) {
//        viewModelScope.launch {
//            try {
//                val options = PhoneAuthOptions.newBuilder(auth)
//                    .setPhoneNumber("+91${phoneNumber}")
//                    .setTimeout(60L, TimeUnit.SECONDS)
//                    .setActivity(activity)
//                    .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//                        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
//                            Log.i("PhoneAuth", phoneAuthCredential.smsCode!!)
////                            auth.signInWithCredential(phoneAuthCredential).let {
////                                if (it.isSuccessful) {
////                                    Log.i("PhoneAuth", phoneAuthCredential.smsCode!!)
////                                    authResult.value = AuthModel(true,phoneAuthCredential.smsCode,"Success")
////                                } else {
////                                    authResult.value = AuthModel(false,null,"auth failed")
////                                }
////                            }
//                        }
//
//                        override fun onVerificationFailed(exception: FirebaseException) {
//                            authResult.value = AuthModel(false,null,"auth failed")
//                            Log.d("onVerificationFailed",exception.message.toString())
//                        }
//                    })
//                    .build()
//                PhoneAuthProvider.verifyPhoneNumber(options)
//            } catch (exception: Exception) {
//                exception.printStackTrace()
//                authResult.value = AuthModel(false,null,"auth failed")
//            }
//        }
//    }
}