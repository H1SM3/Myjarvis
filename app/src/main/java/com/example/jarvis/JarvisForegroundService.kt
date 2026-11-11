package com.example.jarvis

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat

class JarvisForegroundService : Service() {

    private lateinit var appLauncher: AppLauncherEngine

    override fun onCreate() {
        super.onCreate()
        appLauncher = AppLauncherEngine(this)
        startForeground(1, createNotification())
    }

    private fun createNotification(): Notification {
        val channelId = "jarvis_channel"
        val channel = NotificationChannel(
            channelId,
            "Jarvis Background Service",
            NotificationManager.IMPORTANCE_LOW
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("J.A.R.V.I.S. Active")
            .setContentText("Listening for command...")
            .setSmallIcon(android.R.drawable.ic_menu_mic)
            .build()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        appLauncher.destroy()
    }
}
