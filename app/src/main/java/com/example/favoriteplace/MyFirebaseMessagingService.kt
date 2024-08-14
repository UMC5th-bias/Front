package com.example.favoriteplace

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        private const val TAG = "MyFirebaseMsgService"
        private const val CHANNEL_ID = "default_notification_channel_id"
        private const val CHANNEL_NAME = "Favorite Place Notifications"
        private const val NOTIFICATION_ID = 0
    }

    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        createNotificationChannel() // 알림 채널 생성
    }
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "onMessageReceived 함수 호출됨.")

        // 메시지 로깅
        Log.d(TAG, "From: ${remoteMessage.from}")

        remoteMessage.data.isNotEmpty().let {
            // notification
            if(remoteMessage.notification != null) { //포그라운드
                Log.d(TAG, "Notification Message Body: ${remoteMessage.notification?.body}")
            }

            // data
            if (remoteMessage.data.isNotEmpty()) { //백그라운드
                Log.d(TAG, "Data Message: ${remoteMessage.data["type"]}")
            }

            val type = remoteMessage.data["type"]
            val title = remoteMessage.data["title"] ?: "Default title"
            val message = remoteMessage.data["message"] ?: "Default message"

            sendNotification(type, title, message)
        }

    }
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "Refreshed token: $token")
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // 로그인 상태 확인
        val accessToken = sharedPreferences.getString(LoginActivity.ACCESS_TOKEN_KEY, null)
        if (!accessToken.isNullOrEmpty()) {
            sendRegistrationToServer(token)
        }
    }

    private fun sendNotification(type: String?, title: String, message: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtra("type", type) // 타입을 인텐트에 추가
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = getString(R.string.default_notification_channel_id)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = "Default Channel for App Notifications"
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
    private fun sendRegistrationToServer(token: String) {
        // 토큰을 포함하는 요청 객체 생성
        val tokenRequest = TokenRequest(token)

        // Retrofit 클라이언트 사용하여 토큰 서버에 전송
        RetrofitClient.notificationApiService.registerToken(tokenRequest).enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(call: retrofit2.Call<Void>, response: retrofit2.Response<Void>) {
                if (response.isSuccessful) {
                    Log.d(TAG, "Token successfully sent to server.")
                } else {
                    Log.e(TAG, "Failed to send token to server. Response code: ${response.code()}")
                }
            }

            override fun onFailure(call: retrofit2.Call<Void>, t: Throwable) {
                Log.e(TAG, "Failed to send token to server", t)
            }
        })
    }




}