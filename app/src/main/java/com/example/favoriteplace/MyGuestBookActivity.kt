package com.example.favoriteplace

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.favoriteplace.databinding.FragmentMyGuestbookBinding


class MyGuestBookActivity:AppCompatActivity() {
    lateinit var binding: FragmentMyGuestbookBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentMyGuestbookBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}