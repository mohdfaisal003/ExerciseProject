package com.exerciseproject.workmanager

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.rooted.deviceinfo.mvvm.repositories.QuotesRepo
import java.util.*

class ApiWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    private val quotesRepo = QuotesRepo()

    override suspend fun doWork(): Result {
        try {
            val response = quotesRepo.getRandomQuote()
            if (response.isSuccessful) {
                Log.d("value", response.body()?.get(0).toString())
                showNotification(response.body()?.get(0)?.q.toString())
            } else if (response.code() >= 400) {

            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }

        return Result.success()
    }

    @SuppressLint("MissingPermission")
    private fun showNotification(quote: String) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "default_channel_id",
                "Exercise Project Channel",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for API Service Notifications"
            }

            val notificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder =
            NotificationCompat.Builder(applicationContext, "default_channel_id")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Exercise Quote of the Day")
                .setContentText(quote)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(applicationContext)) {
            notify(Random().nextInt(), notificationBuilder.build())
        }
    }
}
