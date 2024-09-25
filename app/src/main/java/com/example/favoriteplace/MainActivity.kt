package com.example.favoriteplace

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.favoriteplace.databinding.ActivityMainBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private lateinit var sharedPreferences: SharedPreferences
    internal var accessToken: String? = null // 액세스 토큰을 저장할 변수
    private val REQUEST_CODE_POST_NOTIFICATIONS = 1

    companion object {
        private const val CHANNEL_ID = "FavoritePlaceChannel"
        private const val CHANNEL_NAME = "Favorite Place Notifications"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // SharedPreferences 초기화
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // 알림으로 전달된 intent가 있는 경우 이를 처리
        handleIntent(intent)

        // 초기 프래그먼트 설정 (백그라운드에서 앱이 열릴 때는 이 부분은 건너뜀)
        if (savedInstanceState == null) {
            setupInitialFragment()
        }
//         초기 프래그먼트 설정 및 기타 설정
//        setupInitialFragment(savedInstanceState)

        createNotificationChannel()
        requestNotificationPermission()
        initBottomNavigation()
        checkAndSendFCMToken()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val postId = intent.getIntExtra("POST_ID", -1)
        val guestbookId = intent.getIntExtra("GUESTBOOK_ID", -1)
        val notificationId = intent.getIntExtra("NOTIFICATION_ID", -1)
        val type = intent.getStringExtra("type")
        Log.d("FCM", "postId 넘어옴 : ${postId}")
        Log.d("FCM", "guestbookId 넘어옴 : ${guestbookId}")
        Log.d("FCM", "notificationId 넘어옴 : ${notificationId}")


        // type이 "post"이고 postId가 유효한 경우 PostDetailActivity로 이동
        if (type == "post" && postId != -1) {
            val detailIntent = Intent(this, PostDetailActivity::class.java).apply {
                putExtra("POST_ID", postId)
            }
            startActivity(detailIntent)
            setSelectedNavItem(HomeFragment())
            return // post일 경우 Activity로 이동했으므로 나머지 로직 실행 안함
        } else if (type == "guestBook" && guestbookId != -1) {
            val detailIntent = Intent(this, MyGuestBookActivity::class.java).apply {
                putExtra("GUESTBOOK_ID", guestbookId)
            }
            startActivity(detailIntent)
            setSelectedNavItem(HomeFragment())
            return // guestBook일 경우 Activity로 이동했으므로 나머지 로직 실행 안함
        }

        // type이 다른 경우에만 프래그먼트로 이동
        type?.let { typeValue ->
            val fragment = when (typeValue) {
                "home" -> HomeFragment()
                "animation" -> RallyHomeFragment()
                "shop" -> ShopMainFragment()
                else -> HomeFragment()
            }
            navigateToFragment(fragment)
            setSelectedNavItem(fragment)
        }
    }

    private fun navigateToFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frameLayout, fragment)
            .commitAllowingStateLoss()
    }

    fun setSelectedNavItem(fragment: Fragment) {
        val itemId = when (fragment) {
            is HomeFragment -> R.id.homeFragment
            is RallyHomeFragment -> R.id.rallyhomeFragment
            is ShopMainFragment -> R.id.shopFragment
            is CommunityMainFragment -> R.id.communityFragment
            is MyFragment -> R.id.myFragment
            else -> R.id.homeFragment
        }
        binding.mainBnv.selectedItemId = itemId
    }

    fun setSelectedNavItem(itemId: Int) {
        binding.mainBnv.selectedItemId = itemId
    }

    private fun initBottomNavigation() {
        binding.mainBnv.setOnItemSelectedListener { item ->
            val fragment = when (item.itemId) {
                R.id.homeFragment -> HomeFragment()
                R.id.rallyhomeFragment -> RallyHomeFragment()
                R.id.communityFragment -> CommunityMainFragment()
                R.id.shopFragment -> ShopMainFragment()
                R.id.myFragment -> MyFragment()
                else -> HomeFragment()
            }
            navigateToFragment(fragment)
            true
        }
    }
    private fun checkAndSendFCMToken() {
        // FCM 토큰 가져오기 및 저장
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task: Task<String> ->
                if (!task.isSuccessful) {
                    Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                    return@addOnCompleteListener
                }
                val token = task.result
                Log.d("FCM", "Current token: $token")
                saveTokenToPrefs(token)

                // 로그인 상태 확인 후 FCM 토큰 전송
                if (isLoggedIn()) {
                    sendRegistrationToServer(token)
                }
            }
    }
    private fun isLoggedIn(): Boolean {
        accessToken = sharedPreferences.getString(LoginActivity.ACCESS_TOKEN_KEY, null)
        return !accessToken.isNullOrEmpty()
//        return false;
    }
    override fun onDestroy() {
        super.onDestroy()
    }

    private fun saveTokenToPrefs(token: String) {
        sharedPreferences.edit().putString("fcm_token", token).apply()
    }

//    private fun setupInitialFragment(savedInstanceState: Bundle?) {
//        if (savedInstanceState == null) {
//            val postId = intent.getIntExtra("POST_ID", -1)
//            val type = intent.getStringExtra("type")
//            Log.d("FCM", "PostId 넘어옴 : ${postId}")
//
//
//            // type이 "post"이고 postId가 유효한 경우 PostDetailActivity로 이동
//            if (type == "post" && postId != -1) {
//                val detailIntent = Intent(this, PostDetailActivity::class.java).apply {
//                    putExtra("POST_ID", postId)
//                }
//                startActivity(detailIntent)
//                setSelectedNavItem(HomeFragment())
//                return // post일 경우 Activity로 이동했으므로 나머지 로직 실행 안함
//            }
//
//            val initialFragment = when (intent.getStringExtra("type")) {
//                "shop" -> ShopMainFragment()
//                "animation" -> RallyHomeFragment()
//                "home" -> HomeFragment()
//                else -> HomeFragment()
//            }
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.main_frameLayout, initialFragment)
//                .commitAllowingStateLoss()
//            setSelectedNavItem(initialFragment)
//        }
//    }

    /*
    fun setSelectedNavItem(private fun setupInitialFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            val initialFragment = when (intent.getStringExtra("type")) {
                "shop" -> ShopMainFragment()
                "animation" -> RallyHomeFragment()
                "home" -> HomeFragment()
                else -> HomeFragment()
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_frameLayout, initialFragment)
                .commitAllowingStateLoss()
            setSelectedNavItem(initialFragment)
        }
    }itemId: Int) {
        binding.mainBnv.selectedItemId = itemId
    }
    */

    private fun setupInitialFragment() {
        // 앱이 처음 실행되었을 때 초기 프래그먼트를 설정
        val initialFragment = HomeFragment() // 기본은 HomeFragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frameLayout, initialFragment)
            .commitAllowingStateLoss()
        setSelectedNavItem(initialFragment)
    }

    fun setRecommendRally(itemId: Int) {
        binding.mainBnv.selectedItemId = itemId
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = CHANNEL_ID
            val channelName = CHANNEL_NAME
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
                Log.d("FCM", "Token = $token")

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
                Log.d("MainActivity", "Notification permission granted.")
            } else {
                Toast.makeText(this, "알림 권한이 거부되었습니다. 알림을 받을 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}