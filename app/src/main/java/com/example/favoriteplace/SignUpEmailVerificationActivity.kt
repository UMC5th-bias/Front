package com.example.favoriteplace

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.favoriteplace.databinding.ActivitySignupEmailVerificationBinding
import com.example.favoriteplace.databinding.ActivitySignupPwdInputBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SignUpEmailVerificationActivity: AppCompatActivity() {

    lateinit var binding: ActivitySignupEmailVerificationBinding
    lateinit var email: String
    private var authCodeCreateTime: Long = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupEmailVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)


        Log.d("SignUp", ">> Starting SignUpEmailVerificationActivity")


        // 사용자가 입력한 email
        val email = intent.getStringExtra("email") ?: ""
        binding.emailEt.setText(email)


        // Retrofit 객체 생성
        val retrofit = Retrofit.Builder()
            .baseUrl("http://favoriteplace.store:8080") // 서버의 base URL을 설정합니다.
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val emailCodeService = retrofit.create(EmailCodeService::class.java)

        // 이메일 서버에 보낸 후 랜덤 인증번호 받아오기
        val request = EmailCodeService.EmailRequest(email)
        val call = emailCodeService.getAuthNumber(request)


        call.enqueue(object : Callback<EmailCodeService.AuthResponse> {
            override fun onResponse(
                call: Call<EmailCodeService.AuthResponse>,
                response: Response<EmailCodeService.AuthResponse>
            ) {
                if (response.isSuccessful) {
                    val authResponse = response.body()
                    val authNum = authResponse?.authNum

                    binding.nextTv.setOnClickListener {
                        val enteredCode = binding.codenumEt.text.toString()

                        if (enteredCode == authNum.toString()) {
                            checkEmail(email, enteredCode)

                        } else {
                            // 인증번호가 다를 경우
                            binding.codenumErrorTv.visibility = View.VISIBLE
                            binding.text1.visibility = View.GONE
                            binding.text2.visibility = View.GONE
                        }
                    }
                }
            }

            override fun onFailure(call: Call<EmailCodeService.AuthResponse>, t: Throwable) {
            }
        })

    }

    private fun checkEmail(email: String, enteredCode: String) {
        if (!isAuthCodeValid()) {
            Log.d("SignUp", ">> Email verification code expired")


            // Retrofit 객체 생성
            val retrofit = Retrofit.Builder()
                .baseUrl("http://favoriteplace.store:8080") // 서버의 base URL을 설정합니다.
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val emailCodeService = retrofit.create(EmailCodeService::class.java)

            // 이메일 인증번호 확인 요청 보내기
            val requestBody = EmailCodeService.EmailCode(email, enteredCode.toInt())
            val checkEmailCall = emailCodeService.checkEmail(requestBody)

            checkEmailCall.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Log.d("SignUp", ">> Email code checked successfully")

                        val snsAllow = intent.getBooleanExtra("snsAllow", false)


                        val intent =
                            Intent(
                                this@SignUpEmailVerificationActivity,
                                SignUpPwdInputActivity::class.java
                            )

                        intent.putExtra("snsAllow", snsAllow)
                        intent.putExtra("email", email)

                        startActivity(intent)


                    } else {
                        Log.d("SignUp", ">> Server response: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    // 요청 실패 처리
                    Log.d("SignUp", ">> Email code check request failed")
                }
            })
        }
    }

    // 인증 코드의 유효 시간 확인 -> 5분
    private fun isAuthCodeValid(): Boolean {
        val currentTime = System.currentTimeMillis()
        val elapsedTime = currentTime - authCodeCreateTime
        val validityDuration = 5 * 60 * 1000 // 5분
        return elapsedTime <= validityDuration
    }

}