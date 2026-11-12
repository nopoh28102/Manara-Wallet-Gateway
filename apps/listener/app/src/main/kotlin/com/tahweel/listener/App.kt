package com.tahweel.listener

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AppLog.init(this)
        createChannel(this)
        KeepAliveWorker.schedule(this)
        AppLog.i("app", "process start v${BuildConfig.VERSION_NAME}")
    }

    companion object {
        const val CHANNEL_ID = "manara-wallet-listener"

        fun createChannel(context: Context) {
            val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
            if (nm.getNotificationChannel(CHANNEL_ID) != null) return
            val channel = NotificationChannel(
                CHANNEL_ID,
                context.getString(R.string.notif_channel),
                NotificationManager.IMPORTANCE_LOW,
            ).apply {
                setShowBadge(false)
                description = "Keeps the SMS forwarder alive"
            }
            nm.createNotificationChannel(channel)
        }
    }
}
