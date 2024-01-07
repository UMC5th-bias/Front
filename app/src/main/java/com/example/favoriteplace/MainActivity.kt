package com.example.favoriteplace

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //fragment라 바로 실행이 불가능해서 확인하고자 넣은 코드. merge 시 삭제 요망
        val RallyCategoryFragment=RallyCategoryFragment()
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout,RallyCategoryFragment).commit()
    }
}