package com.example.favoriteplace

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.example.favoriteplace.databinding.ActivityLoginBinding
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    lateinit var retrofit: Retrofit
    private lateinit var loginService: LoginService

    private lateinit var sharedPreferences: SharedPreferences
    private val handler = Handler()

    companion object {
        const val ACCESS_TOKEN_KEY = "token"
        const val REFRESH_TOKEN_KEY = "refreshToken"
        const val CHECK_TOKEN_INTERVAL = 1000 * 60 * 60 // 1 hour in milliseconds
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrofit 객체 생성
        retrofit = Retrofit.Builder()
            .baseUrl("http://favoriteplace.store:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        loginService = retrofit.create(LoginService::class.java)

        // SharedPreferences 초기화
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // 로그인 버튼 클릭 시
        binding.logoinBtn.setOnClickListener {
            performLogin()
        }

        getKeyHash()

        // 카카오 로그인
        binding.loginKakaoIb.setOnClickListener {
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this@LoginActivity)) {
                UserApiClient.instance.loginWithKakaoTalk(this@LoginActivity) { token, error ->
                    handleKakaoLogin(token, error)
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this@LoginActivity) { token, error ->
                    handleKakaoLogin(token, error)
                }
            }
        }

        binding.loginSignupTv.setOnClickListener {
            startActivity(Intent(this, SignUpConditionConfirmActivity::class.java))
            finish()
        }

        scheduleTokenCheck()
    }

    private fun getKeyHash() {
        try {
            val packageInfo: PackageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in packageInfo.signatures) {
                try {
                    val md: MessageDigest = MessageDigest.getInstance("SHA")
                    md.update(signature.toByteArray())
                    val keyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT)
                    Log.d("KeyHash", keyHash)
                } catch (e: NoSuchAlgorithmException) {
                    Log.e("KeyHash", "Unable to get MessageDigest. signature=$signature", e)
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            Log.e("KeyHash", "KeyHash:null")
        }
    }

    private fun setLoggedIn(isLoggedIn: Boolean) {
        sharedPreferences.edit {
            putBoolean("isLoggedIn", isLoggedIn)
        }
        Log.d("Login", "로그인 상태 변경 : $isLoggedIn")
    }

    private fun scheduleTokenCheck() {
        handler.postDelayed({
            checkTokenValidity()
            scheduleTokenCheck()
        }, CHECK_TOKEN_INTERVAL.toLong())
    }

    private fun checkTokenValidity() {
        val accessToken = sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
        if (accessToken == null || isTokenExpired(accessToken)) {
            performLogin()
        }
    }

    private fun performLogin() {
        val email = binding.logoinInputIdEt.text.toString()
        val password = binding.logoinInputPwdEt.text.toString()

        val call = loginService.login(email, password)
        call.enqueue(object : Callback<LoginService.LoginResponse> {
            override fun onResponse(call: Call<LoginService.LoginResponse>, response: Response<LoginService.LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        val accessToken = loginResponse.accessToken
                        val refreshToken = loginResponse.refreshToken

                        Log.d("Login상태", "AccessToken: $accessToken, \n RefreshToken: $refreshToken")
                        saveToken(accessToken)

                        sharedPreferences.edit {
                            putString(ACCESS_TOKEN_KEY, accessToken)
                            putString(REFRESH_TOKEN_KEY, refreshToken)
                        }
                        setLoggedIn(true)

                        val resultIntent = Intent(this@LoginActivity, MainActivity::class.java).apply {
                            putExtra(ACCESS_TOKEN_KEY, accessToken)
                            putExtra("isLoggedIn", true)
                        }
                        setResult(Activity.RESULT_OK, resultIntent)
                        finish()
                    } else {
                        val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                        Log.e("Login", "Login failed: $errorMessage")
                    }
                } else {
                    Log.e("Login", "Login failed with error code: ${response.code()}")
                    Toast.makeText(this@LoginActivity, "아이디 or 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginService.LoginResponse>, t: Throwable) {
                Log.e("Login", "Login request failed: ${t.message}", t)
            }
        })
    }

    private fun saveToken(token: String) {
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(ACCESS_TOKEN_KEY, token).apply()
    }

    private fun isTokenExpired(token: String): Boolean {
        return false
    }

    // 카카오 로그인 처리 함수
    private fun handleKakaoLogin(token: OAuthToken?, error: Throwable?) {
        if (error != null) {
            Log.e("kakaoLogin", "카카오 계정으로 로그인 실패", error)
        } else if (token != null) {
            Log.d("kakaoLogin", "카카오 계정으로 로그인 성공 ${token.accessToken}")

            // 서버로 토큰 전송
            sendKakaoTokenToServer(token.accessToken, token.refreshToken)
        }
    }

    // 서버로 카카오 토큰 전송
    private fun sendKakaoTokenToServer(accessToken: String, refreshToken: String?) {
        Log.d("kakaoLogin", "----------------------------")
        val tokenData = mapOf(
            "accessToken" to accessToken,
            "refreshToken" to refreshToken
        )
        Log.d("kakaoLogin", "Request Data - Bearer $accessToken , $tokenData")
        val call = loginService.kakaoLogin("Bearer $accessToken", tokenData)

        call.enqueue(object : Callback<LoginService.KakaoLoginResponse> {
            override fun onResponse(call: Call<LoginService.KakaoLoginResponse>, response: Response<LoginService.KakaoLoginResponse>) {
                if (response.isSuccessful) {
                    val kakaoLoginResponse = response.body()
                    Log.d("kakaoLogin", "Response Data - $kakaoLoginResponse")

                    if (kakaoLoginResponse != null) {
                        val receivedAccessToken = kakaoLoginResponse.accessToken
                        val receivedRefreshToken = kakaoLoginResponse.refreshToken

                        Log.d("kakaoLogin", "Received AccessToken: $receivedAccessToken, RefreshToken: $receivedRefreshToken")

                        // 서버에서 받은 토큰 저장
                        saveToken(receivedAccessToken)
                        sharedPreferences.edit {
                            putString(ACCESS_TOKEN_KEY, receivedAccessToken)
                            putString(REFRESH_TOKEN_KEY, receivedRefreshToken)
                        }
                        sharedPreferences.edit {
                            putString(ACCESS_TOKEN_KEY, accessToken)
                            putString(REFRESH_TOKEN_KEY, refreshToken)
                        }
                        setLoggedIn(true)
                    }
                } else {
                    Log.e("kakaoLogin", "카카오 토큰 서버 전송 실패: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<LoginService.KakaoLoginResponse>, t: Throwable) {
                Log.e("kakaoLogin", "카카오 토큰 서버 전송 실패: ${t.message}", t)
            }
        })
    }

}
