package com.example.favoriteplace

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import com.example.favoriteplace.databinding.DialogRallylocationDetailBinding
import com.google.firebase.annotations.concurrent.UiThread
import com.naver.maps.map.util.FusedLocationSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.create


class RallyLocationDetailFragment : Fragment(), OnMapReadyCallback {
    lateinit var binding: FragmentRallylocationdetailBinding
    lateinit var retrofit: Retrofit
    lateinit var rallyLocationDetailService: RallyLocationDetailService
    lateinit var rallyCertifyService: RallyCertifyService
    lateinit var homeService : HomeService
    lateinit var naverMap: NaverMap // NaverMap 변수 선언

    //권한 요청 코드
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

    private var rallyAnimationId: Long = -1 // rallyAnimationId 인스턴스 변수 추가
    private lateinit var targetLocation: LatLng


    // 내위치
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var currentUserLocation: LatLng


    // test
    private val testLatitude: Double = 37.520439
    private val testLongitude: Double = 126.887816



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
            .client(OkHttpClient.Builder().addInterceptor(RetrofitClient.logging).build()) // 로깅 인터셉터 추가
            .build()

        rallyLocationDetailService = retrofit.create(RallyLocationDetailService::class.java)
        rallyCertifyService=retrofit.create(RallyCertifyService::class.java)
        homeService = retrofit.create(HomeService::class.java)


        // SharedPreferences 초기화
        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)


        // 네이버 지도 프래그먼트
        val fm = childFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map_container) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map_container, it).commit()
            }

        mapFragment.getMapAsync(this)
        requestLocationPermission()


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



    private fun fetchRallyInfo(rallyAnimationId: Long) {

        //토큰 유효성
        val token = sharedPreferences.getString("token", null)
        val rallyAnimationId = arguments?.getLong("rallyAnimationId") ?: -1

//        if(token!=null){
        val call = rallyLocationDetailService.getRallyInfo("Bearer $token",rallyAnimationId)
        Log.d("RallyLocationDetail", ">> Bearer : $token ")
        Log.d("rallyAnimationId", ">> fetchRallyInfo: $rallyAnimationId")

        call.enqueue(object : Callback<RallyLocationDetailService.RallyInfo> {
            override fun onResponse(
                call: Call<RallyLocationDetailService.RallyInfo>,
                response: Response<RallyLocationDetailService.RallyInfo>
            ) {
                if(response.isSuccessful){
                    val rallyInfo=response.body()
                    if (rallyInfo != null) {
                        Log.d("rallyLocationDetail", ">> rallyInfo: $rallyInfo")
                        // 서버에서 받아온 목표 위치의 위도와 경도로 targetLocation 설정
                        targetLocation = LatLng(rallyInfo.latitude ?: 0.0, rallyInfo.longitude ?: 0.0)
                        rallyInfo.let {
                            // 수정된 부분: isCertified 값을 true로 설정
                            val CertifiedRallyInfo = rallyInfo.copy(isWritable = true)


                            displayRallyInfo(CertifiedRallyInfo)
                            Log.d("rallyLocationDetail", ">> modifiedRallyInfo: $CertifiedRallyInfo")
                            getCurrentLocation()
                            //etCertify()

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

    private fun getCertify() {
        //val
    }

    // 사용자 현재 위치
    private fun getCurrentLocation(){
        val token = sharedPreferences.getString("token", null)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    //currentUserLocation=LatLng(location.latitude, location.longitude)
                    currentUserLocation = LatLng(testLatitude, testLongitude) // test
                    val distance = currentUserLocation.distanceTo(targetLocation)

                    if(distance <=150){
                        // 거리가 150m 이내인 경우 다이얼로그 보여주기
                        Toast.makeText(context,"성지순례 인증하기 20P를 얻으셨습니다!",Toast.LENGTH_SHORT).show()

                        binding.rallyLocationdetailCv.visibility = View.GONE
                        binding.rallyLocationdetailGuestbookCv.visibility = View.VISIBLE
                        binding.rallyLocationdetailNcountCv.visibility = View.VISIBLE


//                        val jsonObject = JSONObject().apply {
//                            put("latitude", testLatitude)
//                            put("longitude", testLongitude)
//                        }
                        //val jsonRequestBody = RequestBody.create("application/json".toMediaTypeOrNull(), jsonObject.toString())
                        // 헤더에 AccessToken 추가
                        val authorizationHeader = "Bearer $token"

                        val rallyAnimationId = arguments?.getLong("rallyAnimationId") ?: -1

                        Log.d("RallyLocationDetail", " Latitude: $testLatitude, Longitude: $testLongitude ")

                        Log.d("RallyLocationDetail", " 1번")
                        uploadPostRequest(rallyAnimationId, authorizationHeader,testLongitude,testLatitude )
                        Log.d("RallyLocationDetail", " 2번")
//                        showDistanceAlertDialog()
//                        Log.d("RallyLocationDetail", " 3")
                    }else{

                        Toast.makeText(context,"150m 반경에 있지 않습니다.",Toast.LENGTH_SHORT).show()
                        binding.rallyLocationdetailCv.visibility = View.VISIBLE
                        binding.rallyLocationdetailGuestbookCv.visibility = View.GONE
                        binding.rallyLocationdetailNcountCv.visibility = View.GONE

                    }
                }
            }
        }
    }

    private fun uploadPostRequest(
        pilgrimageId: Long,
        authorizationHeader: String,
        longitude: Double,
        latitude: Double
    ) {
        val postData = RallyCertifyService.PostData(longitude, latitude)

        val call = rallyCertifyService.RallyCertify(
            pilgrimageId,
            authorizationHeader,
            postData
        )
        Log.d("RallyLocationDetail", " ４번")
        Log.d("RallyLocationDetail", ">> pilgrimageId: $pilgrimageId")

        call.enqueue(object : Callback<RallyCertifyService.RallyCertifyResponse>{
            override fun onResponse(
                call: Call<RallyCertifyService.RallyCertifyResponse>,
                response: Response<RallyCertifyService.RallyCertifyResponse>
            ) {
                if(response.isSuccessful){
                    val responseData = response.body()
                    if(responseData!=null){
                        Log.d("RallyLocationDetail", " 인증 성공! \n" +
                                "메시지: ${response.body()?.message}")
                    }else{
                        Log.d("RallyLocationDetail", "API Error: ${response.errorBody()?.string()}")
                    }
                }else{
                    val errorBody = response.errorBody()?.string()
                    Log.d("RallyLocationDetail", "API Error: $errorBody")
                }
            }

            override fun onFailure(
                call: Call<RallyCertifyService.RallyCertifyResponse>,
                t: Throwable
            ) {
                Log.e("RallyGuestBookActivity", "Network Error: ${t.message}")
            }
        })
    }


    private fun showDistanceAlertDialog() {
        // SharedPreferences에서 토큰 가져오기
        val token = sharedPreferences.getString("token", null)
        val rallyAnimationId = arguments?.getLong("rallyAnimationId") ?: -1


        Log.d("RallyLocationDetail", ">> user token : $token")
        Log.d("rallyAnimationId", ">> showDistanceAlertDialog: $rallyAnimationId")


        fetchUserNickname(token!!) { nickname ->
            // 닉네임을 가져온 후에 다이얼로그 생성
            val dialog = RallyLocationDialog(nickname,rallyAnimationId)
            dialog.arguments = Bundle().apply {
                putLong("rallyAnimationId", rallyAnimationId)
            }
            dialog.show(childFragmentManager, "RallyLocationDialog")
            Log.d("RallyLocationDetail", ">> userNickname : $nickname")
        }

    }

    // user token을 사용하여 사용자 nickname 가져오기
    private fun fetchUserNickname(token: String, callback: (String) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response   = homeService.getUserInfo("Bearer $token")
                if(response.isSuccessful){
                    val loginResponse  = response.body()
                    val userInfo = loginResponse?.userInfo
                    val nickname = userInfo?.nickname ?: ""
                    callback(nickname)

                }else{
                    Log.e("RallyLocationDetail", "Failed to fetch user info: ${response.code()}")
                }


            }catch (e:Exception){
                Log.e("RallyLocationDetail", "Error fetching user nickname: ${e.message}")
            }
        }
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