package com.example.favoriteplace

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.favoriteplace.databinding.FragmentCommunityFreeCommendBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommunityFreeCommendFragment : Fragment() {

    lateinit var binding: FragmentCommunityFreeCommendBinding
    private var freeCommendData=ArrayList<Comments>()
    private var currentPage=1
    private var isLogIn=true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentCommunityFreeCommendBinding.inflate(inflater,container,false)

        fetchPosts()    //서버에서 내가 쓴 댓글을 가져오는 코드

        return binding.root
    }

    //서버에서 내가 쓴 댓글을 가져오는 코드
    private fun fetchPosts() {

        var accessToken: String? =null

        //로그인 중이라면 토큰을 서버에 전달
        if (isLogIn){
            accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlejcwM0BuYXZlci5jb20iLCJpYXQiOjE3MDc5ODUzNDUsImV4cCI6MTcxMDU3NzM0NX0.xFCNj09c3M0CkiCV_Luqq2w6gjhW2Z6-kJM82NF0MtU"
        }

        RetrofitClient.communityService.getMyComments("Bearer $accessToken",currentPage,10)
            .enqueue(object : Callback<CommunityComment> {
                override fun onResponse(
                    call: Call<CommunityComment>,
                    response: Response<CommunityComment>
                ) {
                    if (response.isSuccessful){
                        if(response.body()?.comment?.isNotEmpty()==true){  //post의 값이 있을 경우
                            //freeCommendData에 데이터를 받아옴
                            response.body()?.let { post ->
                                freeCommendData.addAll(post.comment)
                            }

                            Log.d("CommunityFreeCommendFragment1",response.body().toString())
                            currentPage++   //다음 페이지를 받아오기 위해 현재 페이지를 1 증가 시킴
                            fetchPosts()    //재귀함수

                            //RVA 실행
                            val commendRVAdapter=CommunityFreeCommendRVAdapter(freeCommendData)
                            binding.communityFreeCommendRv.adapter=commendRVAdapter
                            binding.communityFreeCommendRv.layoutManager=LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
                        } else {
                            Log.d("CommunityFreeCommendFragment2",response.body().toString())
                        }
                    }
                }

                override fun onFailure(call: Call<CommunityComment>, t: Throwable) {
                    Log.d("CommunityFreeCommendFragment7","Network Error: ${t.message}")
                }
            })
    }
}