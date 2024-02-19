package com.example.favoriteplace

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.favoriteplace.databinding.FragmentMyGuestbookBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyGuestBookActivity : AppCompatActivity() {
    lateinit var binding: FragmentMyGuestbookBinding

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

        //댓글 리스트 가져오기
        RetrofitAPI.rallyLocationDetailService.getComments(
            authorization = "Bearer ${getAccessToken()}",
            guestbookId = guestBookId
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

        Glide.with(this@MyGuestBookActivity)
            .load(detail.pilgrimage.imageReal)
            .diskCacheStrategy(DiskCacheStrategy.ALL) // 이미지 캐싱 전략
            .error(R.drawable.memberimg) // 로딩 실패 시 표시할 이미지
            .transition(DrawableTransitionOptions.withCrossFade()) // 크로스페이드 효과 적
            .into(binding.myGuestbookAddimgIv) // profileImageIv는 PNG 이미지를 로드할 ImageView의 ID입니다.

        Glide.with(this@MyGuestBookActivity)
            .load(detail.pilgrimage.imageAnime)
            .diskCacheStrategy(DiskCacheStrategy.ALL) // 이미지 캐싱 전략
            .error(R.drawable.memberimg) // 로딩 실패 시 표시할 이미지
            .transition(DrawableTransitionOptions.withCrossFade()) // 크로스페이드 효과 적
            .into(binding.myGuestbookRallyIv) // profileImageIv는 PNG 이미지를 로드할 ImageView의 ID입니다.


        Glide.with(this@MyGuestBookActivity)
            .load(detail.userInfo.profileImageUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL) // 이미지 캐싱 전략
            .error(R.drawable.memberimg) // 로딩 실패 시 표시할 이미지
            .transition(DrawableTransitionOptions.withCrossFade()) // 크로스페이드 효과 적
            .into(binding.myGuestbookProfileCiv) // profileImageIv는 PNG 이미지를 로드할 ImageView의 ID입니다.

        // Coil을 사용하여 SVG 이미지 로딩 - 프로필 타이틀
        val imageLoader = ImageLoader.Builder(this@MyGuestBookActivity)
            .availableMemoryPercentage(0.25) // 사용할 수 있는 메모리 비율 설정
            .crossfade(true) // 크로스페이드 효과 활성화
            .componentRegistry { add(SvgDecoder(this@MyGuestBookActivity)) }
            .build()

        val request = ImageRequest.Builder(this@MyGuestBookActivity)
            .crossfade(true)
            .crossfade(300)
            .data(detail.userInfo.profileTitleUrl)
            .target(binding.myGuestbookBadgeIv) // profileTitleIv는 SVG 이미지를 로드할 ImageView의 ID입니다.
            .build()
        imageLoader.enqueue(request)

        // Coil을 사용하여 SVG 이미지 로딩 - 프로필 아이콘
        val iconRequest = ImageRequest.Builder(this@MyGuestBookActivity)
            .crossfade(true)
            .crossfade(300)
            .data(detail.userInfo.profileIconUrl)
            .target(binding.myGuestbookIconCiv) // profileIconIv는 SVG 이미지를 로드할 ImageView의 ID입니다.
            .build()
        imageLoader.enqueue(iconRequest)

    }


}