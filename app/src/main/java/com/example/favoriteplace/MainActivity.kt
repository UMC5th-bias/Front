package com.example.favoriteplace

import android.content.Context
import android.content.Intent
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)   //초기화
        setContentView(binding.root)


        initBottomNavigation()
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

                }

            }
            false
        }
    }
}