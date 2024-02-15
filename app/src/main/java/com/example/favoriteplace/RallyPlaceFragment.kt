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
import com.google.firebase.annotations.concurrent.UiThread
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RallyPlaceFragment: Fragment(), OnMapReadyCallback {

    lateinit var binding: FragmentRallyplaceBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentRallyplaceBinding.inflate(inflater,container,false)

        var regionList: List<Region> = emptyList()

        val fm = childFragmentManager
        val mapFragment = fm.findFragmentById(R.id.rally_map_mv) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.rally_map_mv, it).commit()
            }

        mapFragment.getMapAsync(this)

        fun setRegionRV() {
            //도쿄
            val tempTyoko = regionList.filter {it.state == "도쿄도" }
            if(tempTyoko.isNotEmpty()) {
                binding.tyokoBT.visibility = View.VISIBLE
                binding.tyokoBT.setOnClickListener {
                    if(binding.tokyoRV.isVisible) binding.tokyoRV.visibility = View.GONE
                    else binding.tokyoRV.visibility = View.VISIBLE
                }
                binding.tokyoRV.adapter = RallyPlaceCityRVAdapter(requireActivity(), tempTyoko.first().detail)
                binding.tokyoRV.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL,false)
            }

            //오사카
            val tempOsaka = regionList.filter {it.state == "오사카" }
            if(tempOsaka.isNotEmpty()) {
                binding.osakaBT.visibility = View.VISIBLE
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
                binding.kyotoBT.visibility = View.VISIBLE
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
                binding.hokkaidoBT.visibility = View.VISIBLE
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
                        Log.d("Retrofit:getRegion()", "Response: ${responseData}")
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


    @UiThread
    override fun onMapReady(naverMap: NaverMap) {
        // ...
    }
}