package com.example.favoriteplace

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.example.favoriteplace.databinding.ActivitySignupConditionConfirmBinding

class SignUpConditionConfirmActivity: AppCompatActivity() {

    lateinit var binding: ActivitySignupConditionConfirmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupConditionConfirmBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // 뒤로가기
        binding.backPageIv.setOnClickListener {
            val intent = Intent(this@SignUpConditionConfirmActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }


        binding.checkbox1.setOnClickListener {
            val isChecked = binding.checkbox1.isChecked
            binding.checkbox2.isChecked = isChecked
            binding.checkbox3.isChecked = isChecked
            binding.checkbox4.isChecked = isChecked

            if (binding.checkbox2.isChecked || binding.checkbox3.isChecked) {
                binding.nextBtn.isEnabled = true
                binding.nextBtn.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this@SignUpConditionConfirmActivity, R.color.main))


                binding.nextBtn.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putBoolean("checkbox", true)
                    Log.d("SignUp", "Value sent: $bundle")

                    val snsAllow = binding.checkbox4.isChecked

                    val intent = Intent(this@SignUpConditionConfirmActivity, SignUpEmailInputActivity::class.java)
                    intent.putExtra("snsAllow", snsAllow)
                    startActivity(intent)
                    finish() // 현재 화면 종료

                }
            } else {
                binding.nextBtn.isEnabled = false
            }
        }
    }
}