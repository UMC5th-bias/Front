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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity :AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    lateinit var retrofit: Retrofit
    lateinit var loginService: LoginService
    private lateinit var sharedPreferences: SharedPreferences

    private val handler = Handler()


    companion object {
        const val ACCESS_TOKEN_KEY = "accessToken"
        const val REFRESH_TOKEN_KEY = "refreshToken"
        const val LOGGED_OUT = "loggedOut"
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
        }


        // 주기적으로 토큰 확인
        scheduleTokenCheck()

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

                        Log.d("Login", "AccessToken: $accessToken, RefreshToken: $refreshToken")


                        saveToken(accessToken)


                        // SharedPreferences 객체 가져오기
                        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                        // SharedPreferences에 액세스 토큰 저장
                        sharedPreferences.edit {
                            putString(ACCESS_TOKEN_KEY, accessToken)
                            putString(REFRESH_TOKEN_KEY, accessToken)
                        }

                        val resultIntent = Intent().apply {
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
                }
            }

            override fun onFailure(call: Call<LoginService.LoginResponse>, t: Throwable) {
                // 네트워크 오류 또는 예외 발생 시 처리
                Log.e("Login", "Login request failed: ${t.message}", t)
            }
        })
    }

    private fun saveToken(token: String) {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("token", token).apply()
    }

    private fun isTokenExpired(token: String): Boolean {
        return false
    }

    override fun onStop() {
        super.onStop()

        // 앱이 종료될 때 로그아웃 상태를 SharedPreferences에 저장
        sharedPreferences.edit().putBoolean(LOGGED_OUT, false).apply()
    }

}