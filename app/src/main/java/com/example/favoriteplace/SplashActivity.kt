package com.example.favoriteplace

import android.content.Intent
import android.os.Binder
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.favoriteplace.databinding.ActivitySplashBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity(){

    lateinit var binding : ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        binding = ActivitySplashBinding.inflate(layoutInflater)   //초기화
        setContentView(binding.root)


//        showSplashScreen1()

        // 코루틴으로 지연처리
        lifecycleScope.launch(Dispatchers.Main) {
            showSplashScreen2()
            delay(2000)

            // MainActivity로 이동
            moveToMainActivity()
        }

    }


    private fun showSplashScreen1() {
        updateSplashVisibility(View.VISIBLE, View.GONE)
    }

    private fun showSplashScreen2() {
        updateSplashVisibility(View.GONE, View.VISIBLE)
    }

    // 스플래시 화면의 가시성 업데이트 함수
    private fun updateSplashVisibility(screen1Visibility: Int, screen2Visibility: Int) {
        binding.splashScreen1Layout.visibility = screen1Visibility
        binding.splashScreen2Layout.visibility = screen2Visibility
    }

    // MainActivity로 이동하는 함수
    private fun moveToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // 이전 화면으로 돌아오지 않도록 SplashActivity 종료
    }
}