package com.example.favoriteplace

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.collection.arraySetOf
import androidx.core.content.edit
import com.example.favoriteplace.HomeFragment.Companion.ACCESS_TOKEN_KEY
import com.example.favoriteplace.HomeFragment.Companion.LOGIN_REQUEST_CODE
import com.example.favoriteplace.databinding.ActivityMainBinding
import com.kakao.sdk.common.util.Utility

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private lateinit var sharedPreferences: SharedPreferences
    internal var accessToken: String? = null // 액세스 토큰을 저장할 변수


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
        accessToken = sharedPreferences.getString(LoginActivity.ACCESS_TOKEN_KEY, null)
        if (accessToken != null) {

            Log.d("MainActivity", ">> login 상태 ")

        } else {
            Log.d("MainActivity", ">> Token 없음 ")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
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
                    checkToken()
                    if(accessToken.isNullOrEmpty()) {
                        Toast.makeText(this, "로그인 후 이용 가능한 메뉴입니다.", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.main_frameLayout, MyFragment())
                            .commitAllowingStateLoss()
                        return@setOnItemSelectedListener true
                    }

                }

            }
            false
        }
    }

    fun setSelectedNavItem(itemId: Int) {
        binding.mainBnv.selectedItemId = itemId
    }

    fun setRecommendRally(itemId: Int) {
        binding.mainBnv.selectedItemId = itemId
    }


}
