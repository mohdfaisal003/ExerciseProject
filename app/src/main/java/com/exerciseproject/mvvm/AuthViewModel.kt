package com.test.project.mvvm

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class AuthViewModel : ViewModel() {

//    val authResult = MutableLiveData<AuthModel>()
//
//    private val auth = FirebaseAuth.getInstance()

//    fun sendVerificationCode(activity: Activity,phoneNumber: String) {
//        viewModelScope.launch {
//            try {
//                PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                    phoneNumber,
//                    60,
//                    TimeUnit.SECONDS,
//                    activity,
//                    object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//                        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
//                            auth.signInWithCredential(phoneAuthCredential).let {
//                                if (it.isSuccessful) {
//                                    Log.i("PhoneAuth", phoneAuthCredential.smsCode!!)
//                                    authResult.value = AuthModel(true,phoneAuthCredential.smsCode,"Success")
//                                } else {
//                                    authResult.value = AuthModel(false,null,"auth failed")
//                                }
//                            }
//                        }
//
//                        override fun onVerificationFailed(exception: FirebaseException) {
//                            authResult.value = AuthModel(false,null,"auth failed")
//                            Log.d("onVerificationFailed",exception.message.toString())
//                        }
//
//                    })
//            } catch (exception: Exception) {
//                exception.printStackTrace()
//                Log.d("PhoneAuthError",exception.message.toString())
//            }
//        }
//
//
//    }

//    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
//        viewModelScope.launch {
//            try {
//                val task = auth.signInWithCredential(credential).await()
//                _authResult.value = task.user != null
//            } catch (e: Exception) {
//                _authResult.value = false
//            }
//        }
//    }

    var verificationId = MutableLiveData<String?>()
    var authResult = MutableLiveData<AuthModel>()
    private val auth = FirebaseAuth.getInstance()

    fun sendVerificationCode(activity: Activity, phoneNumber: String) {
        viewModelScope.launch {
            try {
                val options = PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber("+91${phoneNumber}")
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setActivity(activity)
                    .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                            auth.signInWithCredential(phoneAuthCredential).let {
                                if (it.isSuccessful) {
                                    Log.i("PhoneAuth", phoneAuthCredential.smsCode!!)
                                    authResult.value = AuthModel(true,phoneAuthCredential.smsCode,"Success")
                                } else {
                                    authResult.value = AuthModel(false,null,"auth failed")
                                }
                            }
                        }

                        override fun onVerificationFailed(exception: FirebaseException) {
                            authResult.value = AuthModel(false,null,"auth failed")
                            Log.d("onVerificationFailed",exception.message.toString())
                        }
                    })
                    .build()
                PhoneAuthProvider.verifyPhoneNumber(options)
            } catch (exception: Exception) {
                exception.printStackTrace()
                authResult.value = AuthModel(false,null,"auth failed")
            }
        }
    }
}