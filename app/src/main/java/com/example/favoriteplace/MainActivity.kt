package com.example.favoriteplace

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
                }

                R.id.myFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frameLayout, MyProfileCardFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

            }
            false
        }
    }
}