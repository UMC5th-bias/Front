package com.example.favoriteplace

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.favoriteplace.databinding.FragmentCommunityFreeMyBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommunityFreeMyFragment : Fragment() {

    lateinit var binding: FragmentCommunityFreeMyBinding
    private var freeMyWriteData = ArrayList<Posts>()
    private var currentPage=1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentCommunityFreeMyBinding.inflate(inflater,container,false)

        fetchPosts()    //서버에서 내 글을 가져오는 코드

        return binding.root
    }

    // 사용자의 로그인 상태를 확인하는 메소드
    private fun isLoggedIn(): Boolean {
        return getAccessToken() != null
    }

    private fun getAccessToken(): String? {
        val sharedPreferences = activity?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences?.getString(LoginActivity.ACCESS_TOKEN_KEY, null)
    }

    private fun fetchPosts() {

        var accessToken: String? =null

        //로그인 중이라면 토큰을 서버에 전달
        if (isLoggedIn()){
            accessToken = getAccessToken()
        }

        RetrofitClient.communityService.getMyPosts("Bearer $accessToken",currentPage,10)
            .enqueue(object : Callback<CommunityPost> {

                override fun onResponse(
                    call: Call<CommunityPost>,
                    response: Response<CommunityPost>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.post?.isNotEmpty() == true) {   //post의 값이 있을 경우,
                            response.body()?.let { post ->
                                //freeMyWriteData에 데이터를 받아옴
                                freeMyWriteData.addAll(post.post.map { item ->
                                    Posts(
                                        item.id,
                                        item.title,
                                        item.nickname,
                                        item.views,
                                        item.likes,
                                        item.comments,
                                        item.passedTime
                                    )
                                })

                                currentPage++   //다음 페이지를 받아오기 위해 현재 페이지를 1 증가 시킴
                                fetchPosts()    //재귀함수

                                //RVA실행
                                val communityFreeMyRVAdapter =
                                    CommunityFreeMyRVAdapter(freeMyWriteData, object : CommunityFreeLatelyRVAdapter.OnItemClickListener{
                                        override fun onItemClick(postId: Int) {
                                            val intent = Intent(context, PostDetailActivity::class.java).apply {
                                                putExtra("POST_ID", postId)
                                            }
                                            startActivity(intent)
                                        }
                                    })
                                binding.communityFreeMyRv.adapter = communityFreeMyRVAdapter
                                binding.communityFreeMyRv.layoutManager =
                                    LinearLayoutManager(
                                        context,
                                        LinearLayoutManager.VERTICAL,
                                        false
                                    )
                            }
                        }
                    }
                }
                override fun onFailure(call: Call<CommunityPost>, t: Throwable) {
                    Log.d("CommunityFreeMyFragment","Network Error: ${t.message}")
                }
            })
    }
}