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

        binding = FragmentRallyplaceBinding.inflate(inflater,container,false)

        var regionList: List<Region> = emptyList()

        val placeInfo: Map<String, Map<String, List<RallyPlaceLocationItem>>> =
            mapOf(
                "도쿄" to mapOf(
                    "도쿄도 시부야구" to listOf(
                        RallyPlaceLocationItem(name = "날씨의 아이", location = "시부야 스크램블 교차로", R.drawable.rallyplace_example_image_1),
                        RallyPlaceLocationItem(name = "주술회전", location = "우다가와초 주차장 옆", R.drawable.rallyplace_example_image_2),
                    ),
                    "도쿄도 신주쿠구" to listOf(
                        RallyPlaceLocationItem(name = "날씨의 아이", location = "맥도날드 신주쿠 역전점", R.drawable.rallyplace_example_image_3),
                        RallyPlaceLocationItem(name = "너의 이름은", location = "스가신사 잎구 계단", R.drawable.rallyplace_example_image_4),
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

        fun setRegionRV() {
            //도쿄
            val tempTyoko = regionList.filter {it.state == "도쿄도" }
            binding.tyokoBT.setOnClickListener {
                if(binding.tokyoRV.isVisible) binding.tokyoRV.visibility = View.GONE
                else binding.tokyoRV.visibility = View.VISIBLE
            }
            binding.tokyoRV.adapter = RallyPlaceCityRVAdapter(requireActivity(), tempTyoko.first().detail)
            binding.tokyoRV.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL,false)

            //오사카
            val tempOsaka = regionList.filter {it.state == "오사카" }
            if(tempOsaka.isNotEmpty()) {
                binding.osakaBT.setOnClickListener {
                    if(binding.osakaRV.isVisible) binding.osakaRV.visibility = View.GONE
                    else binding.osakaRV.visibility = View.VISIBLE
                }
                binding.osakaRV.adapter = RallyPlaceCityRVAdapter(requireActivity(), tempOsaka.first().detail)
                binding.osakaRV.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL,false)
            }

            //쿄토
            val tempKyoto = regionList.filter {it.state == "쿄토" }
            if(tempKyoto.isNotEmpty()) {
                binding.kyotoBT.setOnClickListener {
                    if(binding.kyotoRV.isVisible) binding.kyotoRV.visibility = View.GONE
                    else binding.kyotoRV.visibility = View.VISIBLE
                }
                binding.kyotoRV.adapter = RallyPlaceCityRVAdapter(requireActivity(), tempKyoto.first().detail)
                binding.kyotoRV.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL,false)

            }

            //훗카이도
            val tempHokkaido = regionList.filter {it.state == "훗카이도" }
            if(tempHokkaido.isNotEmpty()) {
                binding.hokkaidoBT.setOnClickListener {
                    if(binding.hokkaidoRV.isVisible) binding.hokkaidoRV.visibility = View.GONE
                    else binding.hokkaidoRV.visibility = View.VISIBLE
                }
                binding.hokkaidoRV.adapter = RallyPlaceCityRVAdapter(requireActivity(), tempHokkaido.first().detail)
                binding.hokkaidoRV.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL,false)
            }
        }



        RetrofitAPI.rallyPlaceService.getRegion().enqueue(object: Callback<List<Region>> {
            override fun onResponse(call: Call<List<Region>>, response: Response<List<Region>>) {
                if(response.isSuccessful) {
                    val responseData = response.body()
                    if(responseData != null) {
                        Log.w("Retrofit:getRegion()", "Response: ${responseData}")
                        regionList = responseData
                        regionList.forEach { region ->
                            region.detail.sortedBy { it.id }
                        }
                    }
                    setRegionRV()
                }
                else {
                    Log.e("Retrofit:getRegion()", "notSuccessful: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Region>>, t: Throwable) {
                Log.e("Retrofit:getRegion()", "onFailure: $t")
            }

        })

        return binding.root
    }
}