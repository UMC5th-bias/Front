package com.example.favoriteplace

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
    private var isLogIn=true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentCommunityFreeMyBinding.inflate(inflater,container,false)

        fetchPosts()    //서버에서 내 글을 가져오는 코드

        val mywriteRVAdapter=CommunityFreeMyRVAdapter(freeMyWriteData)
        binding.communityFreeMyRv.adapter=mywriteRVAdapter
        binding.communityFreeMyRv.layoutManager=LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        return binding.root
    }
    private fun fetchPosts() {

        var accessToken: String? =null

        //로그인 중이라면 토큰을 서버에 전달
        if (isLogIn){
            accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzanUwODIyN0BkdWtzdW5nLmFjLmtyIiwiaWF0IjoxNzA3OTY0MjU2LCJleHAiOjE3MTA1NTYyNTZ9.3BlIUX0to5XHybHHUoNPFlraGSA9S3STlMDMwMjOhsc"
        }

        RetrofitClient.communityService.getMyPosts("Bearer $accessToken",currentPage,10)
            .enqueue(object : Callback<CommunityPost> {

                override fun onResponse(
                    call: Call<CommunityPost>,
                    response: Response<CommunityPost>
                ) {
                    if (response.isSuccessful) {
                        if(response.body()?.post?.isNotEmpty() == true){   //post의 값이 있을 경우,
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
                                val communityFreeMyRVAdapter=
                                    CommunityFreeMyRVAdapter(freeMyWriteData)
                                binding.communityFreeMyRv.adapter = communityFreeMyRVAdapter
                                binding.communityFreeMyRv.layoutManager =
                                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
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