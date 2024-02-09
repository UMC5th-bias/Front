package com.example.favoriteplace

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.favoriteplace.databinding.ActivitySignupEmailInputBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SignUpEmailInputActivity: AppCompatActivity() {

    lateinit var binding: ActivitySignupEmailInputBinding
    lateinit var retrofit: Retrofit
    lateinit var emailCodeService: EmailCodeService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupEmailInputBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("SignUp", ">> Starting SignUpEmailInputActivity")

        // Retrofit 객체 생성
        retrofit = Retrofit.Builder()
            .baseUrl("http://favoriteplace.store:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        emailCodeService = retrofit.create(EmailCodeService::class.java)



        // 뒤로가기
        binding.backPageIv.setOnClickListener {
            val intent = Intent(this@SignUpEmailInputActivity, SignUpConditionConfirmActivity::class.java)
            startActivity(intent)
            finish()
        }


        // 이메일 유효성 검사
        binding.emailCheckBtn.setOnClickListener {
            val email = binding.emailInputEt.text.toString()
            if (isEmailValid(email)) {

                // 서버에 이메일 중복 확인 요청
                val request = EmailCodeService.EmailExistRequest(email)
                val call = emailCodeService.checkEmailExistence(request)

                call.enqueue(object : Callback<EmailCodeService.EmailExistResponse> {
                    override fun onResponse(
                        call: Call<EmailCodeService.EmailExistResponse>,
                        response: Response<EmailCodeService.EmailExistResponse>
                    ) {
                        if (response.isSuccessful) {
                            val emailExistResponse = response.body()
                            val isExists = emailExistResponse?.isExists ?: false
                            if (isExists) {
                                Toast.makeText(
                                    this@SignUpEmailInputActivity,
                                    "중복된 이메일입니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                // 중복되지 않은 이메일인 경우
                                val snsAllow = intent.getBooleanExtra("snsAllow", false)


                                val intent = Intent(
                                    this@SignUpEmailInputActivity,
                                    SignUpEmailVerificationActivity::class.java
                                )

                                intent.putExtra("snsAllow", snsAllow)
                                intent.putExtra("email", email)

                                startActivity(intent)
                            }
                        } else {
                            // 요청 실패시
                        }
                    }

                    override fun onFailure(
                        call: Call<EmailCodeService.EmailExistResponse>,
                        t: Throwable
                    ) {
                        //네트워크 오류
                    }
                })
            } else{

                binding.emailErrorIv.visibility = View.VISIBLE
                binding.emailErrorTv.visibility = View.VISIBLE
            }
        }
    }


    // email 유효성 검사
    private fun isEmailValid(email:String):Boolean {
        val emailPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\$".toRegex()
        return emailPattern.matches(email)
    }

}
