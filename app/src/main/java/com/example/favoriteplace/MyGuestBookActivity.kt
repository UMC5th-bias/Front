package com.example.favoriteplace

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.favoriteplace.databinding.FragmentMyGuestbookBinding
import com.google.gson.Gson
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyGuestBookActivity : AppCompatActivity() {
    lateinit var binding: FragmentMyGuestbookBinding
    private lateinit var naverMap: NaverMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentMyGuestbookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 인텐트에서 게시글 ID 가져오기
        val guestBookId = intent.getLongExtra("GUESTBOOK_ID", 0L)
        Log.d("MyGuestBook", "guestBookID : ${guestBookId}")
        if (guestBookId != 0L) {
            fetchPostDetail(guestBookId)
        }

        binding.myGuestbookTv.setOnClickListener {
            finish()
        }


        binding.myGuestbookUploadBtn.setOnClickListener{
            val comment = binding.myGuestbookCommentEt.text.toString()
            if (comment.isNotEmpty()) {
                sendCommentToServer(guestBookId, comment)
        binding.myGuestbookRecommendTv.setOnClickListener {
//            sendLike(guestBookId)
        }

        binding.myGuestbookRecommendTv.setOnClickListener {
//            sendLike(guestBookId)
        }

        //댓글 리스트 가져오기
        getComments(guestBookId)

                // EditText의 내용 지우기
                binding.myGuestbookCommentEt.text.clear()

                // 소프트 키보드 숨기기
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.myGuestbookCommentEt.windowToken, 0)
            } else {
                Toast.makeText(this,"댓글을 입력해주세요.",Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun getComments(guestbookId: Long) {
        RetrofitAPI.rallyLocationDetailService.getComments(
            authorization = "Bearer ${getAccessToken()}",
            guestbookId = guestbookId
        ).enqueue(object: Callback<RallyLocationDetailComments> {
            override fun onResponse(call: Call<RallyLocationDetailComments>, response: Response<RallyLocationDetailComments>) {
                if(response.isSuccessful) {
                    val responseData = response.body()
                    if(responseData != null) {
                        Log.d("getComments()", "Response: ${responseData}")
                        val rallyLocationDetailRVAdapter =
                            RallyLocationDetailRVAdapter(this@MyGuestBookActivity, responseData.comment ?: emptyList())
                        binding.myGuestbookCommentRv.adapter = rallyLocationDetailRVAdapter
                        binding.myGuestbookCommentRv.layoutManager =
                            LinearLayoutManager(this@MyGuestBookActivity, LinearLayoutManager.VERTICAL, false)
                    }
                }
                else {
                    Log.e("getComments()", "notSuccessful: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<RallyLocationDetailComments>, t: Throwable) {
                Log.e("getComments()", "onFailure: $t")
            }

        })
    }

    private fun getAccessToken(): String? {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences?.getString(LoginActivity.ACCESS_TOKEN_KEY, null)
    }

    // 사용자의 로그인 상태를 확인하는 메소드
    private fun isLoggedIn(): Boolean {
        return getAccessToken() != null
    }

    private fun fetchPostDetail(guestBookId: Long) {

        val authorizationHeader: String?

        if (isLoggedIn()) {
            authorizationHeader = "Bearer ${getAccessToken()}"
        } else {
            authorizationHeader = null
        }

        RetrofitClient.communityService.getRallyPostDetail(authorizationHeader, guestBookId).enqueue(object :
            Callback<RallyDetailResponse> {
            override fun onResponse(call: Call<RallyDetailResponse>, response: Response<RallyDetailResponse>) {
                if (response.isSuccessful) {
                    // 데이터 바인딩으로 화면에 데이터 표시
                    Log.d("MyGuestBook", "response : ${response.body()}")
                    response.body()?.let { rallyDetail ->
                        displayPostDetails(rallyDetail)
                    }
                } else {
                    // 오류 처리
                    Log.d("MyGuestBook", "error: ${response.errorBody()}")
                    Toast.makeText(this@MyGuestBookActivity, "게시글 불러오기 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RallyDetailResponse>, t: Throwable) {
                // 네트워크 오류 처리
                Toast.makeText(this@MyGuestBookActivity, "네트워크 오류가 발생했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                Log.d("MyGuestBood", "Network error: ${t.message}")
            }
        })

        //댓글 리스트 가져오기
        getComments(guestBookId)

    }

    private fun displayPostDetails(detail: RallyDetailResponse) {

        binding.myGuestbookTitleTv.text = detail.pilgrimage.name
        binding.recommendRallyTitleTv.text = detail.pilgrimage.name
        binding.myGuestbookCheckTv.text = detail.pilgrimage.completeNumber.toString()
        binding.myGuestbookTotalTv.text = detail.pilgrimage.pilgrimageNumber.toString()
        binding.recommendRallyDetailAddressTv.text = detail.pilgrimage.address

        binding.myGuestbookNameTv.text = detail.userInfo.nickname

        binding.myGuestbookSubjectTv.text = detail.guestBook.title
        binding.myGuestbookTextTv.text = detail.guestBook.content
        binding.myGuestbookRecommendCntTv.text = detail.guestBook.likes.toString()
        binding.myGuestbookCommentCntTv.text = detail.guestBook.comments.toString()
        binding.myGuestbookViewsCntTv.text = detail.guestBook.views.toString()
        binding.myGuestbookTimeTv.text = detail.guestBook.passedTime
        binding.rallyMapPlaceEnTv.text = detail.pilgrimage.addressEn
        binding.rallyMapPlaceJpTv.text = detail.pilgrimage.addressJp


        // RallyDetailResponse 객체에서 해시태그 정보를 가져와 LinearLayout에 추가하는 과정
        val tagsContainer: LinearLayout = findViewById(R.id.my_guestbook_tagbox_ll)

        // 해시태그
        for (tag in detail.guestBook.hashTag) {
            val tagView = TextView(this).apply {
                layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).also {
                    it.setMargins(0, 15, 15, 15) // 마진 설정: 상단, 우측, 하단 마진
                }
                text = "$tag" // 해시태그 텍스트 설정
                setTextColor(ContextCompat.getColor(context, R.color.main)) // 텍스트 색상
                setBackgroundResource(R.drawable.maincolor_edge) // 배경 설정
                setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13f) // 텍스트 크기
                setPadding(15, 4, 15, 4) // 패딩 설정
            }

            // 생성한 TextView를 LinearLayout에 추가
            tagsContainer.addView(tagView)


        }

        if (detail.guestBook.image.isEmpty()) {
            binding.myGuestbookUserimgCl.visibility = View.GONE
        } else {
            Glide.with(this@MyGuestBookActivity)
                .load(detail.guestBook.image[0])
                .diskCacheStrategy(DiskCacheStrategy.ALL) // 이미지 캐싱 전략
                .error(R.drawable.memberimg) // 로딩 실패 시 표시할 기본 이미지
                .transition(DrawableTransitionOptions.withCrossFade()) // 크로스페이드 효과
                .into(binding.myGuestbookAddimgIv) // ImageView의 ID
        }

        val imageUrls = listOf(detail.pilgrimage.imageAnime, detail.pilgrimage.imageReal).filterNotNull() // null이 아닌 URL만 리스트에 추가
        val viewPager = findViewById<ViewPager2>(R.id.my_guestbook_rally_iv)
        val adapter = ImageSliderAdapter(this, imageUrls)
        viewPager.adapter = adapter


        // 프로필 이미지
        Glide.with(this@MyGuestBookActivity)
            .load(detail.userInfo.profileImageUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL) // 이미지 캐싱 전략
            .error(R.drawable.memberimg) // 로딩 실패 시 표시할 이미지
            .transition(DrawableTransitionOptions.withCrossFade()) // 크로스페이드 효과 적
            .into(binding.myGuestbookProfileCiv) // profileImageIv는 PNG 이미지를 로드할 ImageView의 ID입니다.

        // Coil을 사용하여 SVG 이미지 로딩
        val imageLoader = ImageLoader.Builder(this@MyGuestBookActivity)
            .availableMemoryPercentage(0.25) // 사용할 수 있는 메모리 비율 설정
            .crossfade(true) // 크로스페이드 효과 활성화
            .componentRegistry { add(SvgDecoder(this@MyGuestBookActivity)) }
            .build()

        // 프로필 타이틀
        val request = ImageRequest.Builder(this@MyGuestBookActivity)
            .crossfade(true)
            .crossfade(300)
            .data(detail.userInfo.profileTitleUrl)
            .target(binding.myGuestbookBadgeIv) // profileTitleIv는 SVG 이미지를 로드할 ImageView의 ID입니다.
            .build()
        imageLoader.enqueue(request)

        // 프로필 아이콘
        val iconRequest = ImageRequest.Builder(this@MyGuestBookActivity)
            .crossfade(true)
            .crossfade(300)
            .data(detail.userInfo.profileIconUrl)
            .target(binding.myGuestbookIconCiv) // profileIconIv는 SVG 이미지를 로드할 ImageView의 ID입니다.
            .build()
        imageLoader.enqueue(iconRequest)

        // 이 부분은 지도가 준비되는 방식에 따라 다를 수 있으므로, 실제 구현에 맞게 조정 필요
        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.guestbook_rally_place_mv) as MapFragment?
            ?: MapFragment.newInstance().also {
                supportFragmentManager.beginTransaction().add(R.id.guestbook_rally_place_mv, it).commit()
            }
        mapFragment.getMapAsync { naverMap ->
            this.naverMap = naverMap
            // 서버로부터 받은 위도와 경도로 마커 위치 설정 (초기 마커 추가)
            markerAdd(LatLng(detail.pilgrimage.latitude, detail.pilgrimage.longitude), detail.pilgrimage.address)

            // 추가된 마커를 중심으로 지도 카메라 이동
            val cameraUpdate = CameraUpdate.scrollTo(LatLng(detail.pilgrimage.latitude, detail.pilgrimage.longitude))
            naverMap.moveCamera(cameraUpdate)
        }


    }

    // 마커 기록
    private fun markerAdd(latLng: LatLng, title: String) {
        val marker = Marker()
        marker.position = latLng
        marker.captionText = title
        marker.map = naverMap
    }
    private fun sendCommentToServer(guestBookId: Long, commentContent: String) {
        // 댓글 내용을 JSON 형식의 문자열로 변환합니다.
        val jsonComment = "{\"content\": \"$commentContent\"}"

        // RequestBody를 생성하여 JSON 형식의 문자열을 전달합니다.
        val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), jsonComment)

        // 헤더에 AccessToken 추가
        val authorizationHeader = "Bearer ${getAccessToken()}"

        // Retrofit 클라이언트 인터페이스에서 정의한 API 메서드를 사용하여 댓글을 등록하는 요청을 만듭니다.
        RetrofitClient.communityService.postRallyComment(authorizationHeader, guestBookId, requestBody).enqueue(object : Callback<ApplyResponse> {
            override fun onResponse(call: Call<ApplyResponse>, response: Response<ApplyResponse>) {
                if (response.isSuccessful) {
                    Log.e("MyGuestBook", "댓글이 등록되었습니다.")
                    getComments(guestBookId)
                } else {
                    Log.e("MyGuestBook", "댓글 등록에 실패했습니다.")
                }
            }

            override fun onFailure(call: Call<ApplyResponse>, t: Throwable) {
                Log.e("PostDetailActivity", "네트워크 오류: ${t.message}")
            }
        })
    }

    //서버에 추천 보내는 함수
    private fun sendLike(postId: Long){
        // 헤더에 AccessToken 추가
        val authorizationHeader = "Bearer ${getAccessToken()}"

        RetrofitClient.communityService.sendRallyLike(authorizationHeader, postId)
            .enqueue(object : Callback<PostDetail>{
                override fun onResponse(call: Call<PostDetail>, response: Response<PostDetail>) {
                    if (response.isSuccessful){
                        fetchPostDetail(postId) //다시 실행
                    } else {
                        val errorBody = response.errorBody()?.string()
                        val errorResponse = Gson().fromJson(errorBody, PostDetail::class.java)

                        //이 부분 수정 필요, 서버에 전달 예정
                        if (errorResponse != null) {
                            Toast.makeText(
                                applicationContext,
                                "${errorResponse.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                override fun onFailure(call: Call<PostDetail>, t: Throwable) {
                    Log.e("LikePost", "네트워크 오류가 발생했습니다: ${t.message}")
                }

            })
    }


}