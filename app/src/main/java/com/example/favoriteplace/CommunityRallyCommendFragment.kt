package com.example.favoriteplace

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.favoriteplace.databinding.FragmentCommunityRallyCommendBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommunityRallyCommendFragment : Fragment() {

    lateinit var binding: FragmentCommunityRallyCommendBinding
    private var rallyCommendData=ArrayList<MyComments>()
    private var currentPage=1
    private var isLogIn=true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentCommunityRallyCommendBinding.inflate(inflater,container,false)

        fetchPosts()

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

    //서버에서 내가 쓴 댓글을 가져오는 코드
    private fun fetchPosts() {

        var accessToken: String? =null

        //로그인 중이라면 토큰을 서버에 전달
        if (isLoggedIn()){
            accessToken = getAccessToken()
        }

        RetrofitClient.communityService.getMyRallyComment("Bearer $accessToken",currentPage,10)
            .enqueue(object : Callback<RallyMyComment> {
                override fun onResponse(
                    call: Call<RallyMyComment>,
                    response: Response<RallyMyComment>
                ) {
                    if (response.isSuccessful){
                        if(response.body()?.comment?.isNotEmpty()==true){  //comment의 값이 있을 경우
                            //rallyCommendData에 데이터를 받아옴
                            response.body()?.let { post ->
                                rallyCommendData.addAll(post.comment)
                            }

                            currentPage++   //다음 페이지를 받아오기 위해 현재 페이지를 1 증가 시킴
                            fetchPosts()    //재귀함수

                            //RVA 실행
                            val commendRVAdapter=CommunityRallyCommendRVAdapter(rallyCommendData)
                            binding.communityRallyCommendRv.adapter=commendRVAdapter
                            binding.communityRallyCommendRv.layoutManager=
                                LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
                        }
                    }
                }

                override fun onFailure(call: Call<RallyMyComment>, t: Throwable) {
                    Log.d("CommunityRallyCommendFragment7","Network Error: ${t.message}")
                }
            })
    }
}