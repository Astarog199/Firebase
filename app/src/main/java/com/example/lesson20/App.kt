package com.example.lesson20

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.crashlytics.FirebaseCrashlytics


class App: Application() {
    override fun onCreate() {
        super.onCreate()
//        !BuildConfig.DEBUG Это означает, что отчеты об ошибках будут отправляться в Crashlytics в продакшн-среде, но не будут отправляться в debug-сборках приложения.
//        true чтобы отчеты об ошибках отправлялись в Crashlytics
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)

        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.O)
            createNotificationChanel()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChanel(){
        val name = "Test notification channel"
        val descriptionText = "This a simple description"
        val importance = NotificationManager.IMPORTANCE_HIGH

        val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "test_channel_id"
    }

}