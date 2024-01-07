package com.exerciseproject

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory
import com.google.firebase.initialize

class ExerciseProject: Application() {

    override fun onCreate() {
        super.onCreate()
        Firebase.initialize(this)
        FirebaseAnalytics.getInstance(this)

        Firebase.appCheck.installAppCheckProviderFactory(
            SafetyNetAppCheckProviderFactory.getInstance()
        )
    }
}