package com.example.favoriteplace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.favoriteplace.databinding.FragmentRallylocationdetailBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RallyLocationDetailFragment : Fragment(), OnMapReadyCallback {
    lateinit var binding: FragmentRallylocationdetailBinding
    lateinit var retrofit: Retrofit
    lateinit var rallyLocationDetailService: RallyLocationDetailService

    // 내위치
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var currentUserLocation: LatLng

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentRallylocationdetailBinding.inflate(inflater, container, false)

        // Retrofit 객체 생성
        retrofit = Retrofit.Builder()
            .baseUrl("http://favoriteplace.store:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

        rallyLocationDetailService = retrofit.create(RallyLocationDetailService::class.java)


        // 사용자 위치 가져오기를 위한 Fused Location Provider Client 초기화
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // 네이버 지도 프래그먼트
        val fm = childFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map_container) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map_container, it).commit()
            }

        mapFragment.getMapAsync(this)
        Log.d("RallyLocationDetail", ">> 네이버 지도 프래그먼트")
    }


    private fun fetchRallyInfo(naverMap: NaverMap) {
        val call = rallyLocationDetailService.getRallyInfo()
        call.enqueue(object  : Callback<RallyLocationDetailService.RallyInfo> {
            override fun onResponse(
                call: Call<RallyLocationDetailService.RallyInfo>,
                response: Response<RallyLocationDetailService.RallyInfo>
            ) {
                if(response.isSuccessful){
                    val rallyInfo=response.body()
                    Log.d("rallyLocationDetail", ">> rallyInfo: $rallyInfo")
                    rallyInfo?.let { rallyInfo->

                        Log.d("RallyLocationDetail", ">> 실행1 -----------")

                        // 장소까지 거리 계산
                        val distanceToLocation = currentUserLocation.distanceTo(LatLng(rallyInfo.latitude,rallyInfo.longitude))
                        if(distanceToLocation >= 150){
                            // 150m 이내 해당하는 경우
                            binding.rallyLocationdetailCv.isEnabled= true
                            Log.d("RallyLocationDetail", ">> 150m 이내 해당")
                        }else{
                            binding.rallyLocationdetailCv.isEnabled= false
                            Log.d("RallyLocationDetail", ">> 150m 이내 해당하지 않음")
                        }


                        Log.d("RallyLocationDetail", ">> 실행2 -----------")
                        binding.rallyLocationdetailTitleTv.text = rallyInfo?.rallyName
                        binding.rallyLocationdetailNameTv.text =rallyInfo?.rallyName
                        binding.rallyLocationdetailPlaceTv.text = rallyInfo?.address
                        binding.rallyLocationdetailCheckTv.text= rallyInfo?.myPilgrimageNumber.toString()
                        binding.rallyLocationdetailTotalTv.text = rallyInfo?.pilgrimageNumber.toString()

                        Glide.with(requireContext())
                            .load(rallyInfo?.image)
                            .into(binding.rallyLocationdetailAniIv)


                        Glide.with(requireContext())
                            .load(rallyInfo?.realImage)
                            .into(binding.rallyLocationdetailPlaceIv)


                        if (rallyInfo.isMultiWritable) {
                            binding.rallyLocationdetailNcountCv.visibility = View.VISIBLE
                        } else {
                            binding.rallyLocationdetailNcountCv.visibility = View.INVISIBLE
                        }

                        if (rallyInfo.isWritable) {
                            binding.rallyLocationdetailGuestbookCv.visibility = View.VISIBLE
                        } else {
                            binding.rallyLocationdetailNcountCv.visibility = View.INVISIBLE
                        }


                        val latitude = rallyInfo.latitude ?: 0.0
                        val longitude = rallyInfo.longitude ?: 0.0
                        val marker = Marker()
                        marker.position = LatLng(latitude,longitude)
                        marker.map = naverMap

                        val cameraUpdate = CameraUpdate.scrollTo(LatLng(latitude, longitude))
                        naverMap.moveCamera(cameraUpdate)
                    }
                }else{
                    // 응답 실패
                    Log.e("RallyLocationDetail", "RallyLocationDetail failed with error code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<RallyLocationDetailService.RallyInfo>, t: Throwable) {
                Log.d("RallyLocationDetail", ">> server error")
            }
        })
    }

    // 지도 준비 됐을 때 호출하는 콜백 메서드
    override fun onMapReady(naverMap: NaverMap) {
        fetchRallyInfo(naverMap)
    }
}