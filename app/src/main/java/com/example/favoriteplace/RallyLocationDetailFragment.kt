package com.example.favoriteplace

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.favoriteplace.databinding.FragmentRallylocationdetailBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.Manifest
import android.app.AlertDialog
import com.google.firebase.annotations.concurrent.UiThread
import com.naver.maps.map.util.FusedLocationSource


class RallyLocationDetailFragment : Fragment(), OnMapReadyCallback {
    lateinit var binding: FragmentRallylocationdetailBinding
    lateinit var retrofit: Retrofit
    lateinit var rallyLocationDetailService: RallyLocationDetailService
    lateinit var naverMap: NaverMap // NaverMap 변수 선언
    private lateinit var locationSource: FusedLocationSource

    //권한 요청 코드
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

    private var rallyAnimationId: Long = -1 // rallyAnimationId 인스턴스 변수 추가


    // 내위치
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var currentUserLocation: LatLng

    // SharedPreferences
    private lateinit var sharedPreferences: SharedPreferences

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
            .client(createOkHttpClient()) // OkHttpClient 추가
            .build()

        rallyLocationDetailService = retrofit.create(RallyLocationDetailService::class.java)
        // SharedPreferences 초기화
        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)


        // 네이버 지도 프래그먼트
        val fm = childFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map_container) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map_container, it).commit()
            }

        mapFragment.getMapAsync(this)

        //locationSource = FusedLocationSource(this, RallyPlaceFragment.LOCATION_PERMISSION_REQUEST_CODE)


        return binding.root
    }



    private fun requestLocationPermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            // 사용자가 이전에 권한을 거부한 경우
            // 권한이 필요한 이유에 대해 설명하는 다이얼로그를 표시
            AlertDialog.Builder(requireContext())
                .setTitle("위치 권한이 필요합니다")
                .setMessage("이 앱에서는 현재 위치를 사용합니다. 위치 권한을 허용해주세요.")
                .setPositiveButton("확인") { _, _ ->
                    // 권한 요청 다이얼로그 표시
                    requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
                }
                .setNegativeButton("취소") { _, _ ->
                    // 사용자가 권한 요청을 취소한 경우에 대한 처리
                    Log.e("RallyLocationDetail", "사용자가 위치 권한 요청을 거부했습니다.")
                }
                .show()
        } else {
            // 처음으로 권한을 요청하는 경우 또는 사용자가 "다시 묻지 않음"을 체크한 경우
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)





    }



    private fun fetchRallyInfo(rallyAnimationId: Long) {

        //토큰 유효성
        val token = sharedPreferences.getString("token", null)
        val rallyAnimationId = arguments?.getLong("rallyAnimationId") ?: -1

//        if(token!=null){
            val call = rallyLocationDetailService.getRallyInfo("Bearer $token",rallyAnimationId)
            Log.d("RallyLocationDetail", ">> Bearer : $token ")


            call.enqueue(object : Callback<RallyLocationDetailService.RallyInfo> {
                override fun onResponse(
                    call: Call<RallyLocationDetailService.RallyInfo>,
                    response: Response<RallyLocationDetailService.RallyInfo>
                ) {
                    if(response.isSuccessful){
                        val rallyInfo=response.body()
                        if (rallyInfo != null) {
                            Log.d("rallyLocationDetail", ">> rallyInfo: $rallyInfo")
                            rallyInfo.let {
                                displayRallyInfo(rallyInfo)

                            }
                        }else{
                            // 응답 바디가 null인 경우
                            Log.e("RallyLocationDetail", "Response body is null")
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
    private fun displayRallyInfo(rallyInfo: RallyLocationDetailService.RallyInfo) {


        binding.rallyLocationdetailTitleTv.text = rallyInfo.rallyName
        binding.rallyLocationdetailNameTv.text =rallyInfo.rallyName
        binding.rallyLocationdetailPlaceTv.text = rallyInfo.address
        binding.rallyLocationdetailCheckTv.text= rallyInfo.myPilgrimageNumber.toString()
        binding.rallyLocationdetailTotalTv.text = rallyInfo.pilgrimageNumber.toString()

        binding.rallyMapPlaceEnTv.text = rallyInfo.addressEn
        binding.rallyMapPlaceJpTv.text = rallyInfo.addressJp


        Glide.with(requireContext())
            .load(rallyInfo.image)
            .into(binding.rallyLocationdetailAniIv)


        Glide.with(requireContext())
            .load(rallyInfo.realImage)
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
        marker.map = naverMap // 수정된 부분: 네이버 지도에 마커 추가
//
//        // 마커를 중심으로 지도 이동
        val cameraUpdate = CameraUpdate.scrollTo(LatLng(latitude, longitude))
        naverMap.moveCamera(cameraUpdate)
    }


    // 지도 준비 됐을 때 호출
    @UiThread
    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap // naverMap 초기화

        fetchRallyInfo(rallyAnimationId)

    }

    // 사용자 로그인 상태 확인
    private fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .header("Authorization", "Bearer ${sharedPreferences.getString("token", null)}")
            val request = requestBuilder.build()
            chain.proceed(request)
        }.build()
    }
}