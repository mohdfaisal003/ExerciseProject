package com.exerciseproject.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.exerciseproject.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {

    lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val verificationId = intent.getStringExtra("verificationId")
        binding.verificationIdTv.text = verificationId
    }
}