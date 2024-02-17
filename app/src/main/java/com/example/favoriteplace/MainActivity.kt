package com.example.favoriteplace

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.collection.arraySetOf
import com.example.favoriteplace.HomeFragment.Companion.LOGIN_REQUEST_CODE
import com.example.favoriteplace.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)   //초기화
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        // 앱 실행 시 토큰 값 확인
        checkToken()

        initBottomNavigation()
}

    private fun checkToken() {
        val accessToken = sharedPreferences.getString("token", null)
        if (accessToken != null) {

            Log.d("MainActivity", ">> login 상태 ")

        } else {
            Log.d("MainActivity", ">> Token 없음 ")
        }
    }

    override fun onDestroy() {
        // 앱이 종료될 때 토큰 값을 제거
        removeToken()
        super.onDestroy()
    }

    private fun removeToken() {
        sharedPreferences.edit().remove("token").apply()
    }

    private fun initBottomNavigation(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frameLayout, HomeFragment())
            .commitAllowingStateLoss()


        binding.mainBnv.setOnItemSelectedListener { item->
            when (item.itemId){

                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frameLayout, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.rallyhomeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frameLayout, RallyHomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.communityFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frameLayout, CommunityMainFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.shopFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frameLayout, ShopMainFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.myFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frameLayout, MyFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true

                }

            }
            false
        }
    }
}