package com.example.favoriteplace

import android.content.Intent
import android.os.Binder
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.favoriteplace.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity(){

    lateinit var binding : ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        binding = ActivitySplashBinding.inflate(layoutInflater)   //초기화
        setContentView(binding.root)


//        showSplashScreen1()

        // 일정 시간 지연 이후 실행하기 위한 코드
//        Handler(Looper.getMainLooper()).postDelayed({
            showSplashScreen2()
//        }, 2000)

    }


    private fun showSplashScreen1() {
        binding.splashScreen1Layout.visibility = View.VISIBLE
    }


    private fun showSplashScreen2() {

        binding.splashScreen1Layout.visibility = View.GONE
        binding.splashScreen2Layout.visibility = View.VISIBLE

        // 일정 시간 지연 이후 실행하기 위한 코드
        Handler(Looper.getMainLooper()).postDelayed({

            // 일정 시간이 지나면 MainActivity로 이동
            val intent= Intent( this,MainActivity::class.java)
            startActivity(intent)

            // 이전 키를 눌렀을 때 스플래스 스크린 화면으로 이동을 방지하기 위해
            // 이동한 다음 사용안함으로 finish 처리
            finish()

        }, 2000)
    }
}