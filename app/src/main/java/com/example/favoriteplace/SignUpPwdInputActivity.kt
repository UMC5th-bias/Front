package com.example.favoriteplace

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.favoriteplace.databinding.ActivitySignupPwdInputBinding
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SignUpPwdInputActivity: AppCompatActivity() {

    lateinit var binding: ActivitySignupPwdInputBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupPwdInputBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("SignUp", ">> Starting SignUpPwdInputActivity")


        // 뒤로가기
        binding.backIv.setOnClickListener {
            val intent = Intent(this@SignUpPwdInputActivity, SignUpEmailVerificationActivity::class.java)
            startActivity(intent)
            finish()
        }


        binding.nextBtn.setOnClickListener {
            //비밀번호 포함 여부
            if(isPasswordValid(binding.pwdInputEt.text.toString())){
                //유효한 경우
                binding.pwdCheckedIv.visibility = View.VISIBLE
                binding.pwdErrorIv.visibility= View.GONE
                binding.pwdErrorTv.visibility= View.GONE

            }else{
                binding.pwdCheckedIv.visibility = View.GONE
                binding.pwdErrorIv.visibility= View.VISIBLE
                binding.pwdErrorTv.visibility= View.VISIBLE
            }


            //비밀번호 일치 여부
            if(binding.pwdInputEt.text.toString()!=""){
                if(binding.pwdInputEt.text.toString().equals(binding.pwdReinputEt.text.toString())){
                    Log.d("SignUp", "Value received: 일치")
                    binding.repwdCheckedIv.visibility= View.VISIBLE
                    binding.repwdErrorIv.visibility= View.GONE

                    binding.nextBtn.text = "다음 단계"
                    binding.nextBtn.backgroundTintList =
                        ColorStateList.valueOf(ContextCompat.getColor(this@SignUpPwdInputActivity, R.color.main))
                    if(binding.nextBtn.text=="다음 단계"){
                        // 다음 단계 클릭 활성화
                        binding.nextBtn.setOnClickListener {
                            val snsAllow = intent.getBooleanExtra("snsAllow", false)
                            val email = intent.getStringExtra("email") ?:""
                            val password = binding.pwdInputEt.text.toString()

                            val intent =
                                Intent(this@SignUpPwdInputActivity, SignUpProfileSettingActivity::class.java )

                            intent.putExtra("snsAllow", snsAllow)
                            intent.putExtra("email", email)
                            intent.putExtra("password",password)

                            Log.d("SignUp", ">> $snsAllow, $email, $password")

                            startActivity(intent)
                            finish() // 현재 화면 종료
                        }
                    }



                }
            }else{
                Log.d("SignUp", "Value received: 불일치")
                binding.repwdCheckedIv.visibility= View.GONE
                binding.repwdErrorIv.visibility= View.VISIBLE
            }
        }
    }







    // password 유효성 검사
    fun isPasswordValid(password:String):Boolean{
        val passwordPattern = "^(?=.*[0-9])(?=.*[!@#\$%^&*()-+=]).{8,}\$".toRegex()
        return passwordPattern.matches(password)
    }
}