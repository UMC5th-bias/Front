package com.example.favoriteplace

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.favoriteplace.databinding.FragmentRallyplaceBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RallyPlaceFragment: Fragment(){

    lateinit var binding: FragmentRallyplaceBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://favoriteplace.store:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        apiService.getRegion().enqueue(object: Callback<List<Region>> {
            override fun onResponse(call: Call<List<Region>>, response: Response<List<Region>>) {
                if(response.isSuccessful) {
                    val responseData = response.body()
                    if(responseData != null) {
                        Log.w("Retrofit:getRegion()", "Response: ${response}")
                    }
                }
                else {
                    Log.e("Retrofit:getRegion()", "notSuccessful: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Region>>, t: Throwable) {
                Log.e("Retrofit:getRegion()", "onFailure: $t")
            }

        })

//        Log.w("test", "받아온 데이터: ${apiService.getRegion()}")

        binding = FragmentRallyplaceBinding.inflate(inflater,container,false)

        val placeInfo: Map<String, Map<String, List<RallyPlaceLocationItem>>> =
            mapOf(
                "도쿄" to mapOf(
                    "도쿄도 시부야구" to listOf(
                        RallyPlaceLocationItem(name = "날씨의 아이", location = "시부야 스크램블 교차로", R.drawable.rallyplace_example_image_1),
                        RallyPlaceLocationItem(name = "주술회전", location = "우다가와초 주차장 옆", R.drawable.rallyplace_example_image_1),
                        RallyPlaceLocationItem(name = "주술회전", location = "하라주쿠역 앞 계단", R.drawable.rallyplace_example_image_1),
                        RallyPlaceLocationItem(name = "주술회전", location = "패밀리마트 시부야 1초매점", R.drawable.rallyplace_example_image_1),
                        RallyPlaceLocationItem(name = "주술회전", location = "미야사타 제 1육교", R.drawable.rallyplace_example_image_1),
                    ),
                    "도쿄도 신주쿠구" to listOf(
                        RallyPlaceLocationItem(name = "날씨의 아이", location = "맥도날드 신주쿠 역전점", R.drawable.rallyplace_example_image_1),
                        RallyPlaceLocationItem(name = "너의 이름은", location = "스가신사 잎구 계단", R.drawable.rallyplace_example_image_1),
                        RallyPlaceLocationItem(name = "너의 이름은", location = "요츠야 초등학교 골목길", R.drawable.rallyplace_example_image_1),
                        RallyPlaceLocationItem(name = "주술회전", location = "신주쿠역 신주쿠의 눈", R.drawable.rallyplace_example_image_1),
                    ),
                    "도쿄도 분쿄구" to emptyList(),
                    "도쿄도 지요다구" to emptyList(),
                    "도쿄도 미나토구" to emptyList(),
                    "도쿄도 주오구" to emptyList(),
                    "도쿄도 고토구" to emptyList(),
                    "도쿄도 메구로구" to emptyList(),
                ),
                "오사카" to emptyMap(),
                "쿄토" to emptyMap(),
                "훗카이도" to emptyMap(),
            )

        //도쿄
        binding.tyokoBT.setOnClickListener {
            if(binding.tokyoRV.isVisible) binding.tokyoRV.visibility = View.GONE
            else binding.tokyoRV.visibility = View.VISIBLE
        }
        binding.tokyoRV.adapter = RallyPlaceCityRVAdapter(requireActivity(), placeInfo["도쿄"] ?: emptyMap())
        binding.tokyoRV.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL,false)

        //오사카
        binding.osakaBT.setOnClickListener {
            if(binding.osakaRV.isVisible) binding.osakaRV.visibility = View.GONE
            else binding.osakaRV.visibility = View.VISIBLE
        }
        binding.osakaRV.adapter = RallyPlaceCityRVAdapter(requireActivity(), placeInfo["오사카"] ?: emptyMap())
        binding.osakaRV.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL,false)

        //쿄토
        binding.kyotoBT.setOnClickListener {
            if(binding.kyotoRV.isVisible) binding.kyotoRV.visibility = View.GONE
            else binding.kyotoRV.visibility = View.VISIBLE
        }
        binding.kyotoRV.adapter = RallyPlaceCityRVAdapter(requireActivity(), placeInfo["쿄토"] ?: emptyMap())
        binding.kyotoRV.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL,false)

        //훗카이도
        binding.hokkaidoBT.setOnClickListener {
            if(binding.hokkaidoRV.isVisible) binding.hokkaidoRV.visibility = View.GONE
            else binding.hokkaidoRV.visibility = View.VISIBLE
        }
        binding.hokkaidoRV.adapter = RallyPlaceCityRVAdapter(requireActivity(), placeInfo["훗카이도"] ?: emptyMap())
        binding.hokkaidoRV.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL,false)

        return binding.root
    }
}