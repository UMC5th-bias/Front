package com.example.favoriteplace

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.favoriteplace.databinding.ActivityPostDetailBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityPostDetailBinding

    // 사용자의 로그인 상태를 확인하는 메소드
    private fun isLoggedIn(): Boolean {
        return getAccessToken() != null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPostDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButtonIb.setOnClickListener {
            finish()
        }

        binding.detailIv.setOnClickListener {
            modalWithRoundCorner()
        }

        // 인텐트에서 post_id 값을 추출
        val postId = intent.getIntExtra("POST_ID", -1)
        if (postId != -1) {
            fetchPostDetail(postId)
        } else {
            // 적절한 오류 처리 또는 사용자에게 피드백 제공
        }

        binding.commentSubmitButton.setOnClickListener {
            val comment = binding.commentRegisterEt.text.toString()
            if (comment.isNotEmpty()) {
                sendCommentToServer(postId, comment)
            } else {
                Toast.makeText(this,"댓글을 입력해주세요.",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getAccessToken(): String? {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences?.getString(LoginActivity.ACCESS_TOKEN_KEY, null)
    }

    private fun modalWithRoundCorner() {
        val modal = MyFilterBottomSheetFragment().apply {
            setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundCornerBottomSheetDialogTheme)
        }
        modal.show(supportFragmentManager,MyFilterBottomSheetFragment.TAG)
    }

    private fun fetchPostDetail(postId: Int) {

        val authorizationHeader : String?

        if(isLoggedIn()) {
            authorizationHeader = "Bearer ${getAccessToken()}"
        } else {
            authorizationHeader = null
        }


        RetrofitClient.communityService.getFreePostDetail(authorizationHeader, postId).enqueue(object :
            Callback<PostDetailResponse> {
            override fun onResponse(call: Call<PostDetailResponse>, response: Response<PostDetailResponse>) {
                if (response.isSuccessful) {
                    val postDetail = response.body()
                    postDetail?.let {
                        // 데이터 바인딩이 성공적으로 이루어진 후 로그 출력
                        Log.d("PostDetailActivity", "Fetch post detail successful: ${it.userInfo}")
                        Log.d("PostDetailActivity", "Fetch post detail successful: ${it.postInfo}")

                        binding.profileNicknameTv.text = it.userInfo.nickname

                        Glide.with(this@PostDetailActivity)
                            .load(it.userInfo.profileImageUrl)
                            .diskCacheStrategy(DiskCacheStrategy.ALL) // 이미지 캐싱 전략
                            .apply(RequestOptions().circleCrop()) // RequestOptions를 사용하여 circleCrop 적용
                            .error(R.drawable.memberimg) // 로딩 실패 시 표시할 이미지
                            .transition(DrawableTransitionOptions.withCrossFade()) // 크로스페이드 효과 적
                            .into(binding.profileImgIv) // profileImageIv는 PNG 이미지를 로드할 ImageView의 ID입니다.

                        // Coil을 사용하여 SVG 이미지 로딩 - 프로필 타이틀
                        val imageLoader = ImageLoader.Builder(this@PostDetailActivity)
                            .availableMemoryPercentage(0.25) // 사용할 수 있는 메모리 비율 설정
                            .crossfade(true) // 크로스페이드 효과 활성화
                            .componentRegistry { add(SvgDecoder(this@PostDetailActivity)) }
                            .build()

                        val request = ImageRequest.Builder(this@PostDetailActivity)
                            .crossfade(true)
                            .crossfade(300)
                            .data(it.userInfo.profileTitleUrl)
                            .target(binding.profileTagIv) // profileTitleIv는 SVG 이미지를 로드할 ImageView의 ID입니다.
                            .build()
                        imageLoader.enqueue(request)

                        // Coil을 사용하여 SVG 이미지 로딩 - 프로필 아이콘
                        val iconRequest = ImageRequest.Builder(this@PostDetailActivity)
                            .crossfade(true)
                            .crossfade(300)
                            .data(it.userInfo.profileIconUrl)
                            .target(binding.postDetailMyIconIv) // profileIconIv는 SVG 이미지를 로드할 ImageView의 ID입니다.
                            .build()
                        imageLoader.enqueue(iconRequest)

                        binding.contentTitleTv.text = it.postInfo.title
                        binding.contentTv.text = it.postInfo.content
                        binding.viewNumTv.text = it.postInfo.views.toString()
                        binding.goodNumTv.text = it.postInfo.likes.toString()
                        binding.commentNumTv.text = it.postInfo.comments.toString()
                        binding.contentTimeTv.text = it.postInfo.passedTime

                        postDetail?.postInfo?.image?.let { images ->
                            if (images.isNotEmpty()) {
                                val imageViews = listOf(
                                    binding.postDetailImg1Iv,
                                    binding.postDetailImg2Iv,
                                    binding.postDetailImg3Iv,
                                    binding.postDetailImg4Iv,
                                    binding.postDetailImg5Iv
                                )
                                // 이미지 URL 리스트를 반복하면서 ImageView에 이미지 로드
                                images.take(5).forEachIndexed { index, imageUrl ->
                                    Glide.with(this@PostDetailActivity)
                                        .load(imageUrl)
                                        .into(imageViews[index])
                                    imageViews[index].visibility = View.VISIBLE
                                }
                            }
                        }

                    }
                } else {
                    // 응답은 받았으나 성공적이지 않은 경우 (예: HTTP 404, 500 등)
                    Log.e("PostDetailActivity", "Fetch post detail failed: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<PostDetailResponse>, t: Throwable) {
                // 에러 처리
                // 네트워크 요청 자체가 실패한 경우
                Log.e("PostDetailActivity", "Network request failed: ${t.message}")

            }
        })

        // 댓글 정보를 받아오는 Retrofit 요청
        RetrofitClient.communityService.getFreeCommentDetail(authorizationHeader, postId.toLong()).enqueue(object :
            Callback<FreeCommentDetailResponse> {
            override fun onResponse(call: Call<FreeCommentDetailResponse>, response: Response<FreeCommentDetailResponse>) {
                if (response.isSuccessful) {
                    val postDetail = response.body()
                    postDetail?.let {
                        // 데이터를 가져왔으므로 어댑터에 설정
                        val commentAdapter = CommentAdapter(postDetail.comment)
                        binding.commentRv.adapter = commentAdapter
                        binding.commentRv.layoutManager = LinearLayoutManager(this@PostDetailActivity)
                    }
                } else {
                    // 댓글 정보를 받아오는 데 실패한 경우
                    Log.e("PostDetailActivity", "Fetch comment detail failed: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<FreeCommentDetailResponse>, t: Throwable) {
                // 네트워크 요청 자체가 실패한 경우
                Log.e("PostDetailActivity", "Network request failed: ${t.message}")
            }
        })
    }

    private fun sendCommentToServer(postId: Int, commentContent: String) {
        // 댓글 내용을 JSON 형식의 문자열로 변환합니다.
        val jsonComment = "{\"content\": \"$commentContent\"}"

        // RequestBody를 생성하여 JSON 형식의 문자열을 전달합니다.
        val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), jsonComment)

        // 헤더에 AccessToken 추가
        val authorizationHeader = "Bearer ${getAccessToken()}"

        // Retrofit 클라이언트 인터페이스에서 정의한 API 메서드를 사용하여 댓글을 등록하는 요청을 만듭니다.
        RetrofitClient.communityService.postFreeComment(authorizationHeader, postId.toLong(), requestBody).enqueue(object : Callback<ApplyResponse> {
            override fun onResponse(call: Call<ApplyResponse>, response: Response<ApplyResponse>) {
                if (response.isSuccessful) {
                    Log.e("PostDetailActivity", "댓글이 등록되었습니다.")
                    fetchPostDetail(postId)
                } else {
                    Log.e("PostDetailActivity", "댓글 등록에 실패했습니다.")
                }
            }

            override fun onFailure(call: Call<ApplyResponse>, t: Throwable) {
                Log.e("PostDetailActivity", "네트워크 오류: ${t.message}")
            }
        })
    }


}