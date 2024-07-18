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
        private const val CHANNEL_ID = "FavoritePlaceChannel"
        private const val CHANNEL_NAME = "Favorite Place Notifications"
        private const val NOTIFICATION_ID = 0
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        // TODO : 메시지 처리
        Log.d("FCM", "onMessageReceived 함수 호출됨.")

        // 메시지 로깅
        Log.d("FCM", "From: ${remoteMessage.from}")

        // 알림 메시지 처리
        remoteMessage.notification?.let {
            Log.d("FCM", "Notification Message Body: ${it.body}")
            sendNotification(it.title, it.body)
        }

        // 데이터 메시지 처리
        remoteMessage.data.isNotEmpty().let {
            Log.d("FCM", "Data Message: ${remoteMessage.data}")
            handleDataMessage(remoteMessage.data)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "Refreshed token: $token")
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // 로그인 상태 확인
        val accessToken = sharedPreferences.getString(LoginActivity.ACCESS_TOKEN_KEY, null)
        if (!accessToken.isNullOrEmpty()) {
            sendRegistrationToServer(token)
        }
    }

    private fun sendNotification(title: String?, messageBody: String?) {
        // 알림을 클릭했을 때 MainActivity를 여는 Intent 생성
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        // PendingIntent 생성: 알림 클릭 시 실행할 작업 정의
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        // 알림 채널 ID 설정
        val channelId = getString(R.string.default_notification_channel_id)

        // NotificationCompat.Builder를 사용하여 알림 구성
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification) // 알림 아이콘 설정
            .setContentTitle(title) // 알림 제목 설정
            .setContentText(messageBody) // 알림 내용 설정
            .setAutoCancel(true) // 알림 클릭 시 자동으로 제거
            .setContentIntent(pendingIntent) // 알림 클릭 시 실행할 PendingIntent 설정

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())
    }

    private fun handleDataMessage(data: Map<String, String>) {
        // 데이터 메시지 처리
        val title = data["title"]
        val body = data["body"]
        sendNotification(title, body)
    }

    private fun sendRegistrationToServer(token: String) {
        // 토큰을 포함하는 요청 객체 생성
        val tokenRequest = TokenRequest(token)

        // Retrofit 클라이언트 사용하여 토큰 서버에 전송
        RetrofitClient.notificationApiService.registerToken(tokenRequest).enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(call: retrofit2.Call<Void>, response: retrofit2.Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("MyFirebaseMessagingService", "Token successfully sent to server.")
                } else {
                    Log.e("MyFirebaseMessagingService", "Failed to send token to server. Response code: ${response.code()}")
                }
            }

            override fun onFailure(call: retrofit2.Call<Void>, t: Throwable) {
                Log.e("MyFirebaseMessagingService", "Failed to send token to server", t)
            }
        })
    }


}