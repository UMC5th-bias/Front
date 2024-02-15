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
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RallyPlaceFragment: Fragment(), OnMapReadyCallback {

    lateinit var binding: FragmentRallyplaceBinding
    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentRallyplaceBinding.inflate(inflater,container,false)

        var regionList: List<Region> = emptyList()

        //지도 fragment
        val fm = childFragmentManager
        val mapFragment = fm.findFragmentById(R.id.rally_place_mv_container) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.rally_place_mv_container, it).commit()
            }
        mapFragment.getMapAsync(this)

        // 위치 구하는 모듈
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)



        // 불러온 지역에 해당하는 버튼 활성화 및 RecyclerView 설정
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


    //지도 준비됐을때 호출
    @UiThread
    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        naverMap.locationSource = locationSource
        naverMap.locationTrackingMode = LocationTrackingMode.Face // 위치 추적 활성화
        val uiSettings = naverMap.uiSettings
        uiSettings.isLocationButtonEnabled = true // 현위치 버튼 활성화

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions,
                grantResults)) {
            if (!locationSource.isActivated) { // 권한 거부됨
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    //권한 요청 코드
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}