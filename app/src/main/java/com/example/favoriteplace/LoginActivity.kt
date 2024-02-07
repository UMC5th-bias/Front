package com.example.favoriteplace

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.favoriteplace.databinding.ActivityLoginBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class LoginActivity :AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private var loginResponse:LoginResponse?=null

    //login
    private val userViewModel by viewModels<UserViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.logoinBtn.setOnClickListener {
            //Login()

            val email = binding.logoinInputIdEt.text.toString()
            val pwd = binding.logoinSearchPwdTv.text.toString()

            if(email=="abcd@naver.com"||pwd=="qwe123@@"){
                val homeFragment = HomeFragment()

                // FragmentTransaction을 통해 HomeFragment를 추가하거나 교체
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frameLayout, homeFragment, "HomeFragment")
                    .commit()
            }else{
                Log.d("Login","email,pwd 오류")
            }

        }


        binding.loginSignupTv.setOnClickListener {
            startActivity(Intent(this, SignUpConditionConfirmFragment::class.java))

        }
    }

    private fun Login() {
        val email = binding.logoinInputIdEt.text.toString()
        val password = binding.logoinInputPwdEt.text.toString()

        // 로그인 함수 호출
//        login(email, password)
    }

//    private fun login(email: String, password: String) {
//        // Retrofit API 호출
//        lifecycleScope.launch {
//            try {
//                val loginService = RetrofitAPI.loginService
//                val response = loginService.login(email,password)
//
//                if(response.isSuccessful){
//                    loginResponse = response.body()
//                }
//                // 서버 응답 처리
//                handleLoginResponse(response)
//            }catch (e:Exception){
//                // 예외 처리
//                Log.d("Login", "Error during login: ${e.message}")
//            }
//        }
//
//    }

    private fun handleLoginResponse(response: Response<LoginResponse>) {
        if (response.isSuccessful){
            //로그인 성공
            val loginResponse = response.body()
            val accessToken = loginResponse?.accessToken

            // 서버로부터 받은 응답을 가공하여 원하는 형식으로 표시
            val customResponse = """
            |Response Body(성공시) {
            |   "grantType": "Bearer",
            |   "accessToken": "$accessToken",
            |   "refreshToken": "${loginResponse?.refreshToken}"
            |}
        """.trimMargin()

            Log.d("Login", "Custom Response: $customResponse")
            // HomeFragment이동
            goToHomeFragment()


        }else{
            Log.d("Login", "Failed to login. Response code: ${response.code()}, message: ${response.message()}")
        }
    }

    private fun goToHomeFragment() {
        val homeFragment = HomeFragment()


        userViewModel.isLogin = true
        val accessToken = loginResponse?.accessToken ?: ""
        saveTokenToSharedPreferences(accessToken)


        try {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_frameLayout, homeFragment, "HomeFragment")
                .commit()
        } catch (e: Exception) {
            /*Log.e("LoginActivity", "Error replacing fragment: ${e.message}")*/
            e.printStackTrace()
        }

    }

    private fun saveTokenToSharedPreferences(token: String) {
        val sharedPreferences = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("token", token)
        editor.apply()
    }

}