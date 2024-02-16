package com.example.favoriteplace

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.favoriteplace.databinding.FragmentCommunityRallyLatelyBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommunityRallyLatelyFragment : Fragment() {

    lateinit var binding: FragmentCommunityRallyLatelyBinding
    private var rallyLatelyWriteData=ArrayList<GuestBook>()
    private var currentPage=1
    private var isLogIn=true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentCommunityRallyLatelyBinding.inflate(inflater,container,false)

        fetchPosts()

        return binding.root

    }

    //서버에서 최신글을 가져오는 코드
    private fun fetchPosts() {

        var accessToken: String? =null

        //로그인 중이라면 토큰을 서버에 전달
        if (isLogIn){
            accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzanUwODIyN0BkdWtzdW5nLmFjLmtyIiwiaWF0IjoxNzA3OTY0MjU2LCJleHAiOjE3MTA1NTYyNTZ9.3BlIUX0to5XHybHHUoNPFlraGSA9S3STlMDMwMjOhsc"
        }

        RetrofitClient.communityService.getRallyPost("Bearer $accessToken",currentPage,10,"latest")
            .enqueue(object : Callback<RallyPost> {

                override fun onResponse(
                    call: Call<RallyPost>,
                    response: Response<RallyPost>
                ) {
                    if (response.isSuccessful) {
                        if(response.body()?.guestBook?.isNotEmpty() == true){ //post의 값이 있을 경우,
                            response.body()?.let { guestbook ->
                                //rallyLatelyWriteData에 데이터를 받아옴
                                rallyLatelyWriteData.addAll(guestbook.guestBook.map { item ->
                                    GuestBook(
                                        item.id,
                                        item.title,
                                        item.nickname,
                                        item.thumbnail,
                                        item.views,
                                        item.likes,
                                        item.comments,
                                        item.passedTime,
                                        item.hashTags
                                    )
                                })

                                currentPage++   //다음 페이지를 받아오기 위해 현재 페이지를 1 증가 시킴
                                fetchPosts()    //재귀함수

                                //RVA실행
                                val latelywriteRVAdapter =
                                    CommunityRallyLatelyRVAdapter(rallyLatelyWriteData)
                                binding.communityRallyLatelyRv.adapter = latelywriteRVAdapter
                                binding.communityRallyLatelyRv.layoutManager =
                                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                            }
                        }
                    }
                }
                override fun onFailure(call: Call<RallyPost>, t: Throwable) {
                    Log.d("CommunityRallyLatelyFragment","Network Error: ${t.message}")
                }
            })
    }
}