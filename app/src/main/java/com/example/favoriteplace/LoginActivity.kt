package com.example.favoriteplace

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.example.favoriteplace.databinding.ActivityLoginBinding
import androidx.lifecycle.lifecycleScope
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.launch
import android.os.Handler
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class
LoginActivity :AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    lateinit var retrofit: Retrofit
    lateinit var loginService: LoginService
    private lateinit var sharedPreferences: SharedPreferences
    private val handler = Handler()


    companion object {
        const val ACCESS_TOKEN_KEY = "token"
        const val REFRESH_TOKEN_KEY = "refreshToken"
        const val CHECK_TOKEN_INTERVAL = 1000 * 60 * 60 // 1 hour in milliseconds
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Retrofit 객체 생성
        retrofit = Retrofit.Builder()

            .baseUrl("http://favoriteplace.store:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

        loginService = retrofit.create(LoginService::class.java)

        // SharedPreferences 초기화
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)


        // 로그인 버튼 틀릭 시
        binding.logoinBtn.setOnClickListener {
            performLogin()
        }


        binding.loginSignupTv.setOnClickListener {
            startActivity(Intent(this, SignUpConditionConfirmActivity::class.java))
            finish() // 현재 화면 종료
        }


        // 주기적으로 토큰 확인
        scheduleTokenCheck()

    }


    // 로그인 상태 sharedPreferences저장
    private fun setLoggedIn(isLoggedIn: Boolean) {
        sharedPreferences.edit {
            putBoolean("isLoggedIn", isLoggedIn)
        }
        Log.d("Login", " 로그인 상태 변경 : $isLoggedIn")
    }

    private fun scheduleTokenCheck() {
        handler.postDelayed({
            checkTokenValidity()
            scheduleTokenCheck() // 주기적으로 호출
        }, CHECK_TOKEN_INTERVAL.toLong())
    }

    private fun checkTokenValidity() {
        val accessToken = sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
        if (accessToken == null || isTokenExpired(accessToken)) {
            // 토큰이 만료되었거나 없는 경우
            // 로그인 다시 수행
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
                        // 로그인 성공 시 AccessToken과 RefreshToken을 처리/ 저장
                        val accessToken = loginResponse.accessToken
                        val refreshToken = loginResponse.refreshToken

                        Log.d("Login상태", "AccessToken: $accessToken, \n RefreshToken: $refreshToken")
                        Log.d("Login상태", ">> lodgin body : $loginResponse")
                        saveToken(accessToken)


                        // SharedPreferences 객체 가져오기
                        // SharedPreferences에 액세스 토큰 저장
                        sharedPreferences.edit {
                            putString(ACCESS_TOKEN_KEY, accessToken)
                            putString(REFRESH_TOKEN_KEY, refreshToken)
                        }
                        // 로그인 상태 변경
                        setLoggedIn(true)

                        // FCM 토큰을 서버에 등록
                        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val fcmToken = task.result
                                sendRegistrationToServer(fcmToken)
                            }
                        }

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
                    // 응답 실패
                    Log.e("Login", "Login failed with error code: ${response.code()}")
                    Toast.makeText(this@LoginActivity,"아이디 or 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<LoginService.LoginResponse>, t: Throwable) {
                // 네트워크 오류 또는 예외 발생 시 처리
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

    private fun sendRegistrationToServer(token: String) {
        val tokenRequest = TokenRequest(token)
        RetrofitClient.notificationApiService.registerToken(tokenRequest).enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(call: retrofit2.Call<Void>, response: retrofit2.Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("LoginActivity", "Token successfully sent to server.")
                } else {
                    Log.e("LoginActivity", "Failed to send token to server. Response code: ${response.code()}")
                }
            }

            override fun onFailure(call: retrofit2.Call<Void>, t: Throwable) {
                Log.e("LoginActivity", "Failed to send token to server", t)
            }
        })
    }

}