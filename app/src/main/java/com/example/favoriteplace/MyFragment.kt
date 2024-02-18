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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyFragment : Fragment(){
    lateinit var binding: FragmentMyBinding

    private lateinit var sharedPreferences: SharedPreferences
    private var userToken: String = ""

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

        fun checkLoginStatus() {
            // SharedPreferences에서 액세스 토큰 가져오기
            val sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            userToken = sharedPreferences.getString(LoginActivity.ACCESS_TOKEN_KEY, "") ?: ""

            if (userToken.isNotEmpty()) {
                Log.d("MyFragment", ">> 로그인 상태입니다.")
            }else{
                // 비회원 상태인 경우
                Log.d("MyFragment", ">> 비회원 상태입니다.")
            }
        }

        binding.myLogoutTv.setOnClickListener {

            checkLoginStatus() //유저 인증정보 가져오기

            if(userToken.isEmpty()) return@setOnClickListener

            RetrofitAPI.myService.logout("Bearer $userToken").enqueue(object:
                Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if(response.isSuccessful) {
                        val responseData = response.body()
                        if(responseData != null) {
                            Log.d("Retrofit:logout()", "Response: ${responseData}")
                        }
                    }
                    else {
                        Log.e("Retrofit:logout()", "notSuccessful: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("Retrofit:logout()", "onFailure: $t")
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

            })
        }

        return binding.root
    }

}