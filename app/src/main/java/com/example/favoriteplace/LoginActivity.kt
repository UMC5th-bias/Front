package com.example.favoriteplace

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.favoriteplace.databinding.ActivityLoginBinding
import androidx.lifecycle.lifecycleScope
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity :AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    lateinit var retrofit: Retrofit
    lateinit var loginService: LoginService


    companion object {
        const val LOGIN_REQUEST_CODE = 101
        const val ACCESS_TOKEN_KEY = "accessToken"
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


        // 로그인 버튼 틀릭 시
        binding.logoinBtn.setOnClickListener {
            val email = binding.logoinInputIdEt.text.toString()
            val password = binding.logoinInputPwdEt.text.toString()


            lifecycleScope.launch {
                try {
                    val response = loginService.login(email,password)
                    if(response.isSuccessful){
                        val loginResponse = response.body()
                        if(loginResponse!=null){
                            // 로그인 성공 시 AccessToken과 RefreshToken을 처리/ 저장
                            val accessToken = loginResponse.accessToken
                            val refreshToken = loginResponse.refreshToken
                            Log.d("Login", "AccessToken: $accessToken, RefreshToken: $refreshToken")


                            val resultIntent = Intent().apply {
                                putExtra(ACCESS_TOKEN_KEY, accessToken)
                            }
                            setResult(Activity.RESULT_OK, resultIntent)
                            finish()

                        }else{
                            val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                            Log.e("Login", "Login failed: $errorMessage")
                        }
                    }else{
                        // 응답 실패
                        Log.e("Login", "Login failed with error code: ${response.code()}")
                    }
                } catch (e:Exception){
                    // 네트워크 오류 또는 예외 발생 시 처리
                    Log.e("Login", "Login request failed: ${e.message}", e)
                }
            }
        }


        binding.loginSignupTv.setOnClickListener {
            startActivity(Intent(this, SignUpConditionConfirmActivity::class.java))
        }
    }




}