package com.example.favoriteplace

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.favoriteplace.databinding.FragmentRallyGuestbookBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RallyGuestBookFragment:Fragment() {
    lateinit var binding : FragmentRallyGuestbookBinding
    private lateinit var retrofit: Retrofit
    private lateinit var rallyLocationDetailService: RallyLocationDetailService

    private lateinit var rallyAdapter: RallyGuestBookVPAdapter // rallyAdapter 선언


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentRallyGuestbookBinding.inflate(inflater,container,false)

        // Retrofit 객체 생성
        retrofit = Retrofit.Builder()
            .baseUrl("http://favoriteplace.store:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        rallyLocationDetailService = retrofit.create(RallyLocationDetailService::class.java)

        val rallyId = arguments?.getLong("rallyId")


       rallyAdapter = RallyGuestBookVPAdapter(this)
        binding.rallyGuestbookVp.adapter=rallyAdapter
        binding.rallyGuestbookVp.orientation= ViewPager2.ORIENTATION_HORIZONTAL


        return binding.root
    }



}