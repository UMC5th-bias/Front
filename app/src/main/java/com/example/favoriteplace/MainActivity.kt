package com.example.favoriteplace

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //fragment 테스트용
        val rallyPlaceFragment = RallyPlaceFragment()
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, rallyPlaceFragment).commit()
    }
}