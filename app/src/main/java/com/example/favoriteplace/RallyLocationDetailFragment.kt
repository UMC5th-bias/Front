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
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.firebase.annotations.concurrent.UiThread
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import org.json.JSONObject
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompHeader
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume


class RallyLocationDetailFragment : Fragment(), OnMapReadyCallback {
    lateinit var binding: FragmentRallylocationdetailBinding
    lateinit var retrofit: Retrofit
    lateinit var rallyLocationDetailService: RallyLocationDetailService
    lateinit var rallyCertifyService: RallyCertifyService
    lateinit var homeService : HomeService
    lateinit var naverMap: NaverMap // NaverMap 변수 선언
    var baseUrl = "http://favoriteplace.store:8080"

    //권한 요청 코드
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

    private var rallyAnimationId: Long = -1 // rallyAnimationId 인스턴스 변수 추가
    private lateinit var targetLocation: LatLng


    // 내위치
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var currentUserLocation: LatLng

    // STOMP 클라이언트
    private lateinit var stompClient: StompClient

    // test
    private val testLatitude: Double = 37.520439
    private val testLongitude: Double = 126.887816

    // 5초마다 소켓으로 위도, 경도 전송 활성화 유무
    private var isLocationEventEnabled = true


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
            .baseUrl(baseUrl)
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

        // 기본적으로 안보임으로 설정
        binding.rallyLocationdetailCv.visibility = View.INVISIBLE
        binding.rallyLocationdetailGuestbookCv.visibility = View.INVISIBLE
        binding.rallyLocationdetailNcountCv.visibility = View.INVISIBLE


        return binding.root
    }

    @SuppressLint("CheckResult")
    private fun connectStompClient(pilgrimageId: Int) {
        // OkHttp 클라이언트 설정
        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        // STOMP 클라이언트 초기화
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://favoriteplace.store:8080/ws")

        // SharedPreferences에서 토큰 가져오기
        val token = sharedPreferences.getString("token", null)
        Log.d("RallyLocationDetail", ">> token: $token")

        // 웹소켓 연결 시도
        val headers = listOf(StompHeader("Authorization", "Bearer $token"))
        stompClient.connect(headers)

        // WebSocket 연결 성공 처리
        stompClient.lifecycle().subscribe { lifecycleEvent: LifecycleEvent ->
            when (lifecycleEvent.type) {
                LifecycleEvent.Type.OPENED -> {
                    Log.d("WebSocket", "연결 성공")
                    // 이벤트 구독 설정
                    subscribeToEvents(pilgrimageId)
                }
                LifecycleEvent.Type.ERROR -> {
                    Log.e("WebSocket", "연결 실패", lifecycleEvent.exception)
                    lifecycleEvent.exception?.printStackTrace() // 자세한 오류 로그 출력
                }
                LifecycleEvent.Type.CLOSED -> {
                    Log.d("WebSocket", "연결 종료")
                }

                else -> {
                    Log.d("WebSocket", "이벤트: ${lifecycleEvent.type}")
                }
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun subscribeToEvents(pilgrimageId: Int) {
        // connect & location 이벤트 구독
        Log.d("RallyLocationDetail", "connect & location 이벤트 구독하기 >> pilgrimageId: $pilgrimageId")
        stompClient.topic("/pub/statusUpdate/$pilgrimageId").subscribe { topicMessage ->
            Log.d("Websocket Received", "/pub/statusUpdate/$pilgrimageId: ${topicMessage.payload}")
            try {
                val jsonObject = JSONObject(topicMessage.payload)
                val rallyLocationDetailStatusUpdate = RallyLocationDetailStatusUpdate(
                    certifyButtonEnabled = jsonObject.getBoolean("certifyButtonEnabled"),
                    guestbookButtonEnabled = jsonObject.getBoolean("guestbookButtonEnabled"),
                    multiGuestbookButtonEnabled = jsonObject.getBoolean("multiGuestbookButtonEnabled")
                )

                Log.d("RallyLocationDetail", "[Websocket] certifyButtonEnabled: ${rallyLocationDetailStatusUpdate.certifyButtonEnabled}")
                Log.d("RallyLocationDetail", "[Websocket] guestbookButtonEnabled: ${rallyLocationDetailStatusUpdate.guestbookButtonEnabled}")
                Log.d("RallyLocationDetail", "[Websocket] multiGuestbookButtonEnabled: ${rallyLocationDetailStatusUpdate.multiGuestbookButtonEnabled}")

                // 인증 불가 에러 메시지 보이기
                if(!rallyLocationDetailStatusUpdate.certifyButtonEnabled
                    && !rallyLocationDetailStatusUpdate.guestbookButtonEnabled
                    && !rallyLocationDetailStatusUpdate.multiGuestbookButtonEnabled) {
                    binding.rallyLocationdetailErrorCv.visibility = View.VISIBLE
                }
                else {
                    binding.rallyLocationdetailErrorCv.visibility = View.GONE
                }

                // 인증하기 버튼 활성화 여부
                if (rallyLocationDetailStatusUpdate.certifyButtonEnabled) {
                    binding.rallyLocationdetailCv.visibility = View.VISIBLE
                }
                else {
                    binding.rallyLocationdetailCv.visibility = View.GONE
                }

                // 방명록(Guestbook) 쓰기 버튼 활성화 여부
                if (rallyLocationDetailStatusUpdate.guestbookButtonEnabled) {
                    binding.rallyLocationdetailGuestbookCv.visibility = View.VISIBLE
                }
                else {
                    binding.rallyLocationdetailGuestbookCv.visibility = View.GONE
                }

                // 다회차 인증글 쓰기 버튼 활성화 여부
                if (rallyLocationDetailStatusUpdate.multiGuestbookButtonEnabled) {
                    binding.rallyLocationdetailNcountCv.visibility = View.VISIBLE
                }
                else {
                    binding.rallyLocationdetailNcountCv.visibility = View.GONE
                }

            } catch (e: Exception) {
                Log.e("RallyLocationDetail", "Error parsing JSON: ${e.message}")
            }
        }

        // certify 이벤트 구독
        Log.d("RallyLocationDetail", "certify 이벤트 구독하기 >> pilgrimageId: $pilgrimageId")
        stompClient.topic("/pub/certify/$pilgrimageId").subscribe { topicMessage ->
            Log.d("Received", topicMessage.payload)
            showDistanceAlertDialog()
        }

        // connect 이벤트 발행 (최초 1회만)
        Log.d("RallyLocationDetail", "connect 이벤트 발행하기 >> pilgrimageId: $pilgrimageId")
        stompClient.send("/app/connect/$pilgrimageId").subscribe(
            {
                Log.d("RallyLocationDetail", "connect 이벤트 발행 성공")
            }, {
                Log.e("RallyLocationDetail", "connect 이벤트 발행 실패", it)

            }
        )

        // 15초마다 location 이벤트 구독 및 메시지 발행 (비동기)
        isLocationEventEnabled = true
        CoroutineScope(Dispatchers.IO).launch {
            while (isLocationEventEnabled) {
                Log.d("RallyLocationDetail", "location 이벤트 발행하기 >> pilgrimageId: $pilgrimageId")

                // 위치 전송이 완료된 후에 다음 작업을 실행
                sendLocation(pilgrimageId)

                // 위치 전송 후 15초 대기
                delay(15000)
            }
        }
    }

    private suspend fun sendLocation(pilgrimageId: Int) {
        return suspendCancellableCoroutine { continuation ->
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    location?.let {
                        stompClient.send(
                            "/app/location/$pilgrimageId",
//                            "{\"latitude\": ${location.latitude}, \"longitude\": ${location.longitude} }"
                            "{\"latitude\": $testLatitude, \"longitude\": $testLongitude }" //test
                        ).subscribe(
                            {
                                Log.d("RallyLocationDetail", "location 이벤트 발행 성공")
                                // 전송이 완료되었으면 continuation을 완료시켜줌
                                continuation.resume(Unit)
                            },
                            { error ->
                                Log.e("RallyLocationDetail", "location 이벤트 발행 실패", error)
                                // 에러 발생 시 continuation도 완료시켜줌
                                continuation.resume(Unit)
                            }
                        )
                    } ?: continuation.resume(Unit) // 위치가 null인 경우에도 종료 처리
                }
            } else {
                // 권한이 없는 경우 즉시 종료 처리
                continuation.resume(Unit)
            }
        }
    }

    private  fun requestLocationPermission() {
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

        val call = rallyLocationDetailService.getRallyInfo("Bearer $token",rallyAnimationId)
        Log.d("RallyLocationDetail", ">> Bearer : $token ")
        Log.d("rallyAnimationId", ">> fetchRallyInfo: $rallyAnimationId")

        call.enqueue(object : Callback<RallyLocationDetailService.RallyInfo> {
            @SuppressLint("CheckResult")
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

                        }
                        // STOMP 클라이언트 연결
                        connectStompClient(rallyAnimationId.toInt())

                        // 성지순례 인증하기 버튼 설정
                        binding.rallyLocationdetailCv.setOnClickListener() {
                            // certify 이벤트 발행 (성지순례 인증하기)
                            Log.d("RallyLocationDetail", "certify 이벤트 발행하기 >> pilgrimageId: $rallyAnimationId.toInt()")
                            stompClient.send("/app/certify/${rallyAnimationId.toInt()}").subscribe(
                                {
                                    Log.d("RallyLocationDetail", "certify 이벤트 발행 성공")
                                }, {
                                    Log.e("RallyLocationDetail", "certify 이벤트 발행 실패", it)

                                }
                            )
                        }

                        // 성지순례 방명록 쓰러가기 버튼 설정
                        binding.rallyLocationdetailGuestbookCv.setOnClickListener() {
                            Log.d("rallyAnimationId", "방명록 쓰러가기 >> $rallyAnimationId")
                            val intent = Intent(requireContext(), RallyGuestBookActivity::class.java)
                            intent.putExtra("rallyAnimationId", rallyAnimationId)
                            startActivity(intent)
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
        binding.rallyLocationdetailNameTv.text = rallyInfo.rallyName
        binding.rallyLocationdetailPlaceTv.text = rallyInfo.address
        binding.rallyLocationdetailCheckTv.text = rallyInfo.myPilgrimageNumber.toString()
        binding.rallyLocationdetailTotalTv.text = rallyInfo.pilgrimageNumber.toString()

        binding.rallyMapPlaceEnTv.text = rallyInfo.addressEn
        binding.rallyMapPlaceJpTv.text = rallyInfo.addressJp


        Glide.with(requireContext())
            .load(rallyInfo.image)
            .into(binding.rallyLocationdetailAniIv)


        Glide.with(requireContext())
            .load(rallyInfo.realImage)
            .into(binding.rallyLocationdetailPlaceIv)


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

    override fun onDestroy() {
        super.onDestroy()
        Log.d("RallyLocationDetail", "onDestroy: 소켓 연결 종료")
        stompClient.disconnect() // 소켓 연결 종료
        isLocationEventEnabled = false // 주기적으로 소켓으로 위도, 경도 전송 활성화 중지
    }

    override fun onPause() {
        super.onPause()
        Log.d("RallyLocationDetail", "onPause: 소켓 연결 종료")
        stompClient.disconnect() // 소켓 연결 종료
        isLocationEventEnabled = false // 주기적으로 소켓으로 위도, 경도 전송 활성화 중지
    }

    override fun onStop() {
        super.onStop()
        Log.d("RallyLocationDetail", "onStop: 소켓 연결 종료")
        stompClient.disconnect() // 소켓 연결 종료
        isLocationEventEnabled = false // 주기적으로 소켓으로 위도, 경도 전송 활성화 중지
    }

    override fun onResume() {
        super.onResume()
        Log.d("RallyLocationDetail", "onResume: fetchRallyInfo()")
        fetchRallyInfo(rallyAnimationId)
    }


}