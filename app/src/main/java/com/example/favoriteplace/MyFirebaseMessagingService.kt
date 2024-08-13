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


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "onMessageReceived 함수 호출됨.")

        // 메시지 로깅
        Log.d(TAG, "From: ${remoteMessage.from}")

//        // 알림 메시지 처리
//        remoteMessage.notification?.let {
//            Log.d("FCM", "Notification Message Body: ${it.body}")
//            sendNotification(it.title, it.body)
//        }
//
//        // 데이터 메시지 처리
//        remoteMessage.data.isNotEmpty().let {
//            Log.d("FCM", "Data Message: ${remoteMessage.data}")
//            handleDataMessage(remoteMessage.data)
//        }

        val type = remoteMessage.data["type"]

        // 적절한 인텐트를 생성하여 특정 화면으로 이동
        val intent = when (type) {
            "home" -> Intent(this, MainActivity::class.java)
//            "notification_center" -> Intent(this, NotificationCenterActivity::class.java)
            "animation" -> Intent(this, RallyHomeFragment::class.java)
            "shop" -> Intent(this, ShopMainFragment::class.java)
            else -> Intent(this, MainActivity::class.java)
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val title = remoteMessage.data["title"]
        val messageBody = remoteMessage.data["message"]

        sendNotification(title, messageBody, pendingIntent)

//        // notification
//        if(remoteMessage.notification != null) { //포그라운드
//            Log.d(TAG, "Notification Message Body: ${remoteMessage.notification?.body}")
//            sendNotification(remoteMessage.notification?.title!!, remoteMessage.notification?.body!!);
//        }
//
//        // data
//        if (remoteMessage.data.isNotEmpty()) { //백그라운드
//            Log.d(TAG, "Data Message: ${remoteMessage.data["body"]}")
//            remoteMessage.data["title"]?.let { sendNotification(it, remoteMessage.data["body"]!!) };
//        }
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

    private fun sendNotification(title: String?, messageBody: String?, pendingIntent: PendingIntent) {
        Log.d(TAG, "sendNotification 호출")

//        // 알림을 클릭했을 때 MainActivity를 여는 Intent 생성
//        val intent = Intent(this, MainActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//
//        // PendingIntent 생성: 알림 클릭 시 실행할 작업 정의
//        val pendingIntent = PendingIntent.getActivity(
//            this, 0, intent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
//        )
////
////        // 알림 채널 생성
//        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
////
////        // Android 8.0 이상에서는 알림 채널을 설정해야 합니다.
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
////            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
////            notificationManager.createNotificationChannel(channel)
////        }
//
//        // NotificationCompat.Builder를 사용하여 알림 구성
//        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
//            .setSmallIcon(R.drawable.ic_notification) // 알림 아이콘 설정
//            .setContentTitle(title) // 알림 제목 설정
//            .setContentText(messageBody) // 알림 내용 설정
//            .setAutoCancel(true) // 알림 클릭 시 자동으로 제거
//            .setContentIntent(pendingIntent) // 알림 클릭 시 실행할 PendingIntent 설정
//
//        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

//    private fun handleDataMessage(data: Map<String, String>) {
//        // 데이터 메시지 처리
//        val title = data["title"]
//        val body = data["body"]
//        sendNotification(title, body)
//    }

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