package com.example.favoriteplace

import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.replace
import com.example.favoriteplace.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
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

                //지금 상태에서 fragment_rallycategory.xml을 볼 수가 없어서 RallyCategoryFragment와 연결.
                //나중에 RallyMain 클래스 작성하면 RallyCategoryFragment()를 해당 클래스 이름으로 변경.
                R.id.rallyhomeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frameLayout, RallyCategoryFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                //지금 상태에서 fragment_community_free.xml을 볼 수가 없어서 CommunityFreeFragment와 연결.
                //나중에 CommunityHome 클래스 작성하면 CommunityFreeFragmentFragment()를 해당 클래스 이름으로 변경.
                R.id.communityFragment->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frameLayout, CommunityRallyFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.shopFragment-> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frameLayout, ShopBannerNewFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }





            }
            false
        }
    }


}