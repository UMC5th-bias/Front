package com.example.favoriteplace


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.favoriteplace.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)



        //광고 배너
        val bannerAdapter = BannerVPAdapter(this)
        binding.homeBannerVp.adapter=bannerAdapter
        binding.homeBannerVp.orientation=ViewPager2.ORIENTATION_HORIZONTAL
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_banner1))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_banner1))




        //로그인 버튼
        binding.homeLoginBtn.setOnClickListener {
            startActivity(Intent(activity, LoginActivity::class.java))
        }

        return binding.root

    }



}