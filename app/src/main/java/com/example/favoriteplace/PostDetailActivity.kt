package com.example.favoriteplace

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.favoriteplace.databinding.ActivityPostDetailBinding

class PostDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityPostDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPostDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButtonIb.setOnClickListener {
            finish()
        }

        binding.detailIv.setOnClickListener {
            modalWithRoundCorner()
        }
    }

    private fun modalWithRoundCorner() {
        val modal = MyFilterBottomSheetFragment().apply {
            setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundCornerBottomSheetDialogTheme)
        }
        modal.show(supportFragmentManager,MyFilterBottomSheetFragment.TAG)
    }
}