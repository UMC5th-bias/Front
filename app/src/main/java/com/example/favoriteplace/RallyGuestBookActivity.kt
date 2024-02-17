package com.example.favoriteplace

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.favoriteplace.databinding.FragmentRallyGuestbookBinding
import retrofit2.Retrofit

class RallyGuestBookActivity : AppCompatActivity() {
    private lateinit var binding : FragmentRallyGuestbookBinding
    lateinit var retrofit: Retrofit


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentRallyGuestbookBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}