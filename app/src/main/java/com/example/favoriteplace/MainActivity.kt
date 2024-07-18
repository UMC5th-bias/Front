package com.example.favoriteplace

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import com.example.favoriteplace.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private lateinit var sharedPreferences: SharedPreferences
    internal var accessToken: String? = null // 액세스 토큰을 저장할 변수
    private val REQUEST_CODE_POST_NOTIFICATIONS = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Firebase 초기화
        FirebaseApp.initializeApp(this)

        binding = ActivityMainBinding.inflate(layoutInflater)   //초기화
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        // 앱 실행 시 토큰 값 확인
        checkToken()

        // 알림 채널을 생성
        createNotificationChannel()

        // 알림 권한 요청
        requestNotificationPermission()

        // 로그인 상태 확인 후 FCM 토큰 전송
        if (isLoggedIn()) {
            val fcmToken = sharedPreferences.getString("fcm_token", null)
            fcmToken?.let {
                sendRegistrationToServer(it)
            } ?: run {
                FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val token = task.result
                        saveTokenToPrefs(token)
                        sendRegistrationToServer(token)
                    } else {
                        Log.d("FCM Token", "FCM Token not yet generated.")
                    }
                }
            }
        }

//        // FCM 토큰 갱신 처리
//        if (isLoggedIn()) {
//            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    val fcmToken = task.result
//                    sendRegistrationToServer(fcmToken)
//                }
//            }
//        }

        initBottomNavigation()
}

    private fun checkToken() {
        accessToken = sharedPreferences.getString(LoginActivity.ACCESS_TOKEN_KEY, null)
        if (accessToken != null) {
            Log.d("MainActivity", ">> login 상태 ")
        } else {
            Log.d("MainActivity", ">> Token 없음 ")
        }
    }

    private fun isLoggedIn(): Boolean {
        return !accessToken.isNullOrEmpty()
    }
    override fun onDestroy() {
        super.onDestroy()
    }

    private fun saveTokenToPrefs(token: String) {
        sharedPreferences.edit().putString("fcm_token", token).apply()
    }
    private fun initBottomNavigation(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frameLayout, HomeFragment())
            .commitAllowingStateLoss()


        binding.mainBnv.setOnItemSelectedListener { item->
            when (item.itemId){

                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frameLayout, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.rallyhomeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frameLayout, RallyHomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.communityFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frameLayout, CommunityMainFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.shopFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frameLayout, ShopMainFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.myFragment -> {
                    checkToken()
                    if(accessToken.isNullOrEmpty()) {
                        Toast.makeText(this, "로그인 후 이용 가능한 메뉴입니다.", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.main_frameLayout, MyFragment())
                            .commitAllowingStateLoss()
                        return@setOnItemSelectedListener true
                    }

                }

            }
            false
        }
    }

    fun setSelectedNavItem(itemId: Int) {
        binding.mainBnv.selectedItemId = itemId
    }

    fun setRecommendRally(itemId: Int) {
        binding.mainBnv.selectedItemId = itemId
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = getString(R.string.default_notification_channel_id)
            val channelName = "Default Channel"
            val channelDescription = "Default Channel for App Notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendRegistrationToServer(token: String) {
        val tokenRequest = TokenRequest(token)
        RetrofitClient.notificationApiService.registerToken(tokenRequest).enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(call: retrofit2.Call<Void>, response: retrofit2.Response<Void>) {
                Log.d("FCM", "token = ${token}")

                if (response.isSuccessful) {
                    Log.d("MainActivity", "Token successfully sent to server.")
                } else {
                    Log.e("MainActivity", "Failed to send token to server. Response code: ${response.code()}")
                }
            }

            override fun onFailure(call: retrofit2.Call<Void>, t: Throwable) {
                Log.e("MainActivity", "Failed to send token to server", t)
            }
        })
    }

    private fun setLoggedIn(isLoggedIn: Boolean) {
        sharedPreferences.edit {
            putBoolean("isLoggedIn", isLoggedIn)
        }
        Log.d("MainActivity", "로그인 상태 변경: $isLoggedIn")
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), REQUEST_CODE_POST_NOTIFICATIONS)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_POST_NOTIFICATIONS) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // 권한이 허용되었습니다.
                Log.d("MainActivity", "Notification permission granted.")
            } else {
                // 권한이 거부되었습니다.
                Toast.makeText(this, "알림 권한이 거부되었습니다. 알림을 받을 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}