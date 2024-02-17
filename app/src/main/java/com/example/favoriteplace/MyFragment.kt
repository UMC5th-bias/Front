package com.example.favoriteplace

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.favoriteplace.databinding.FragmentMyBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MyFragment : Fragment(){
    lateinit var binding: FragmentMyBinding

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyBinding.inflate(inflater, container, false)

        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)



        // 프로필 카드 환경설정
        binding.mySettingIv.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.main_frameLayout, MySettingFragment())
                addToBackStack(null)
            }
        }


        // 고객센터
        binding.myCsTv.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.main_frameLayout, MyCsFragment())
                addToBackStack(null)
            }
        }

        binding.myLogoutTv.setOnClickListener {
            sharedPreferences.edit {
                putBoolean("isLoggedIn", false)
                putString(LoginActivity.ACCESS_TOKEN_KEY, null)
                putString(LoginActivity.REFRESH_TOKEN_KEY, null)
            }

            // 홈 fragment로 이동
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.main_frameLayout, HomeFragment())
            transaction.commit()
            requireActivity().findViewById<BottomNavigationView>(R.id.main_bnv)
                .selectedItemId = R.id.homeFragment

            Toast.makeText(context, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

}