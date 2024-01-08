package com.exerciseproject.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.work.Constraints
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.exerciseproject.databinding.ActivityDashboardBinding
import com.exerciseproject.mvvm.QuotesViewModel
import com.exerciseproject.workmanager.ApiWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class DashboardActivity : AppCompatActivity() {

    lateinit var binding: ActivityDashboardBinding
    val quoteViewModel by viewModels<QuotesViewModel>()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val verificationId = intent.getStringExtra("verificationId")
        quoteViewModel.getRandomQuote()
        quoteViewModel.quotes.observe(this, Observer {
            Log.d("Quotes",it.toString())
            binding.quoteTv.text = it.q
        })

        binding.startServiceBtn.setOnClickListener {
            when {
                ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                        PackageManager.PERMISSION_GRANTED -> {
                    scheduleApiWorker()
                }

                shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                    Toast.makeText(this, "The user denied the notifications permission", Toast.LENGTH_SHORT)
                        .show()
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val uri: Uri = Uri.fromParts("com.exerciseproject", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }

                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) scheduleApiWorker()
            else Toast.makeText(this, "The user denied the notifications permission", Toast.LENGTH_SHORT)
                .show()
        }

    private fun scheduleApiWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(androidx.work.NetworkType.CONNECTED)
            .build()

        val apiWorkRequest: WorkRequest =
            PeriodicWorkRequestBuilder<ApiWorker>(45, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build()

        WorkManager.getInstance(applicationContext).enqueue(apiWorkRequest)
    }
}