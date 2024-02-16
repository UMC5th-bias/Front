package com.example.favoriteplace

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.favoriteplace.databinding.FragmentCommunityRallyMyBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommunityRallyMyFragment : Fragment() {

    lateinit var binding: FragmentCommunityRallyMyBinding
    private var rallyMyWriteData=ArrayList<GuestMy>()
    private var currentPage=1
    private var isLogIn=true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentCommunityRallyMyBinding.inflate(inflater,container,false)

        fetchPosts()

        return binding.root
    }
    private fun fetchPosts() {

        var accessToken: String? =null

        //로그인 중이라면 토큰을 서버에 전달
        if (isLogIn){
            accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlejcwM0BuYXZlci5jb20iLCJpYXQiOjE3MDc5ODUzNDUsImV4cCI6MTcxMDU3NzM0NX0.xFCNj09c3M0CkiCV_Luqq2w6gjhW2Z6-kJM82NF0MtU"
        }

        RetrofitClient.communityService.getMyRally("Bearer $accessToken",currentPage,10)
            .enqueue(object : Callback<RallyMy> {

                override fun onResponse(
                    call: Call<RallyMy>,
                    response: Response<RallyMy>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.myGuestBookInfo?.isNotEmpty() == true) { //post의 값이 있을 경우,
                            response.body()?.let { guestBook ->
                                //rallyMyWriteData에 데이터를 받아옴
                                rallyMyWriteData.addAll(guestBook.myGuestBookInfo.map { item ->
                                    GuestMy(
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
                                val mywriteRVAdapter=CommunityRallyMyRVAdapter(rallyMyWriteData)
                                binding.communityRallyMyRv.adapter=mywriteRVAdapter
                                binding.communityRallyMyRv.layoutManager=
                                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)

                            }
                        }
                    }
                }
                override fun onFailure(call: Call<RallyMy>, t: Throwable) {
                    Log.d("CommunityRallyMyFragment","Network Error: ${t.message}")
                }
            })
    }
}