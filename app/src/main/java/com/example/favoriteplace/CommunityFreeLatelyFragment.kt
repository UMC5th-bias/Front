package com.example.favoriteplace

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.favoriteplace.databinding.FragmentCommunityFreeLatelyBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommunityFreeLatelyFragment : Fragment() {

    lateinit var binding: FragmentCommunityFreeLatelyBinding
    private var freeLatelyWriteData = ArrayList<Posts>()
    private var currentPage=1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentCommunityFreeLatelyBinding.inflate(inflater,container,false)

        fetchPosts()    //서버에서 최신글을 가져오는 코드

        return binding.root
    }

    //서버에서 최신글을 가져오는 코드
    private fun fetchPosts() {
        RetrofitClient.communityService.getPosts(currentPage,10,"latest")
            .enqueue(object : Callback<CommunityPost> {

                override fun onResponse(
                    call: Call<CommunityPost>,
                    response: Response<CommunityPost>
                ) {
                    if (response.isSuccessful) {
                        if(response.body()?.post?.isNotEmpty() == true){   //post의 값이 있을 경우,
                            response.body()?.let { post ->
                                    //freeLatelyWriteData에 데이터를 받아옴
                                    freeLatelyWriteData.addAll(post.post.map { item ->
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
                                    val latelywriteRVAdapter =
                                        CommunityFreeLatelyRVAdapter(freeLatelyWriteData)
                                    binding.communityFreeLatelyRv.adapter = latelywriteRVAdapter
                                    binding.communityFreeLatelyRv.layoutManager =
                                        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                            }
                        }
                    }
                }
                override fun onFailure(call: Call<CommunityPost>, t: Throwable) {
                    Log.d("CommunityFreeLatelyFragment","Network Error: ${t.message}")
                }
            })
    }

}