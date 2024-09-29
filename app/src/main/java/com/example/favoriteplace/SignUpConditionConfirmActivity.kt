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
        binding.backIv.setOnClickListener {
            val intent = Intent(this@SignUpConditionConfirmActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }


        // nextBtn 활성화 여부를 확인하는 함수
        val checkNextBtnState = {
            val isEnabled = binding.checkbox1.isChecked || (binding.checkbox2.isChecked && binding.checkbox3.isChecked) // checkbox1이 체크되었거나 checkbox2와 checkbox3가 모두 체크되었는지 확인
            binding.nextBtn.isEnabled = isEnabled
            binding.nextBtn.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    this@SignUpConditionConfirmActivity,
                    if (isEnabled) R.color.main else R.color.gray
                )
            )
        }


        // checkbox1 클릭 리스너 설정
        binding.checkbox1.setOnClickListener {
            val isChecked = binding.checkbox1.isChecked
            binding.checkbox2.isChecked = isChecked
            binding.checkbox3.isChecked = isChecked
            binding.checkbox4.isChecked = isChecked
            checkNextBtnState() // 상태 변경 후 nextBtn 상태 확인
        }

// checkbox4 클릭 리스너 설정
        binding.checkbox4.setOnClickListener {
            if (!binding.checkbox4.isChecked) {
                binding.checkbox1.isChecked = false
                checkNextBtnState() // 상태 변경 후 nextBtn 상태 확인
            }
        }

// checkbox2와 checkbox3 클릭 리스너 설정
        val checkboxes = listOf(binding.checkbox2, binding.checkbox3)
        checkboxes.forEach { checkbox ->
            checkbox.setOnClickListener {
                checkNextBtnState()
            }
        }

        checkNextBtnState() // 초기 상태 설정

// nextBtn 클릭 리스너 설정
        binding.nextBtn.setOnClickListener {
            val bundle = Bundle().apply {
                putBoolean("checkbox", true)
            }
            Log.d("SignUp", "Value sent: $bundle")

            val snsAllow = binding.checkbox4.isChecked
            val intent = Intent(this@SignUpConditionConfirmActivity, SignUpEmailInputActivity::class.java).apply {
                putExtra("snsAllow", snsAllow)
            }
            startActivity(intent)
            finish() // 현재 화면 종료
        }

    }
}