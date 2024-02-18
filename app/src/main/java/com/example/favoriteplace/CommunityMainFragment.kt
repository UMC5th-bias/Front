package com.example.favoriteplace

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.favoriteplace.databinding.FragmentCommunityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommunityMainFragment: Fragment() {

    lateinit var binding: FragmentCommunityMainBinding
    // postDetail로 이동하는 함수

    private fun navigateToPostDetail(postId: Int) {
        val intent = Intent(activity, PostDetailActivity::class.java).apply {
            putExtra("POST_ID", postId) // 인텐트에 글의 고유 ID 추가
        }
        startActivity(intent) // PostDetailActivity 시작
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommunityMainBinding.inflate(inflater,container,false)

        val topicBoard: Map<String, List<TextView>> = mapOf(
            "today" to listOf(
                binding.topicToday1,
                binding.topicToday2,
                binding.topicToday3,
                binding.topicToday4,
                binding.topicToday5,
            ),
            "month" to listOf(
                binding.topicMonthly1,
                binding.topicMonthly2,
                binding.topicMonthly3,
            )
        )

        var freeTopic: CommunityHomeTrendingFree
        var guestbookTopic: CommunityHomeTrendingGuestbook
        var monthlyTopic: List<CommunityHomeTrendingMonthUnit>


        // 오늘의 화제글 - 자유(홈) 불러오기
        fun setFreeBoard() {
            RetrofitAPI.communityHomeService.getTrendingFree().enqueue(object: Callback<CommunityHomeTrendingFree> {
                override fun onResponse(call: Call<CommunityHomeTrendingFree>, response: Response<CommunityHomeTrendingFree>) {
                    if(response.isSuccessful) {
                        val responseData = response.body()
                        if(responseData != null) {
                            Log.d("Retrofit:getTrendingFree()", "Response: ${responseData}")
                            freeTopic = responseData
                            for(i in 0..4) {
                                if((freeTopic.rank.size ?: 0) > i) {
                                    topicBoard["today"]?.get(i)?.visibility = View.VISIBLE
                                    topicBoard["today"]?.get(i)?.text = freeTopic.rank[i].title
                                    topicBoard["today"]?.get(i)?.tag = responseData.rank[i].id.toInt() // 글의 고유 ID를 tag에 저장
                                    topicBoard["today"]?.get(i)?.setOnClickListener {
                                        // 클릭 시 postDetail 프래그먼트로 이동
                                        val postId = it.tag as Int // tag에서 글의 고유 ID를 가져옴
                                        navigateToPostDetail(postId)
                                    }
                                }
                                else {
                                    topicBoard["today"]?.get(i)?.visibility = View.GONE
                                }
                            }
                        }
                    }
                    else {
                        Log.e("Retrofit:getTrendingFree()", "notSuccessful: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<CommunityHomeTrendingFree>, t: Throwable) {
                    Log.e("Retrofit:getTrendingFree()", "onFailure: $t")
                }

            })
        }

        // 오늘의 화제글 - 성지순례(홈) 불러오기
        fun setGuestbookBoard() {
            RetrofitAPI.communityHomeService.getTrendingGuestbook().enqueue(object: Callback<CommunityHomeTrendingGuestbook> {
                override fun onResponse(call: Call<CommunityHomeTrendingGuestbook>, response: Response<CommunityHomeTrendingGuestbook>) {
                    if(response.isSuccessful) {
                        val responseData = response.body()
                        if(responseData != null) {
                            Log.d("Retrofit:getTrendingGuestbook()", "Response: ${responseData}")
                            guestbookTopic = responseData
                            for(i in 0..4) {
                                if((guestbookTopic.rank.size ?: 0) > i) {
                                    topicBoard["today"]?.get(i)?.visibility = View.VISIBLE
                                    topicBoard["today"]?.get(i)?.text = guestbookTopic.rank[i].title
                                    topicBoard["today"]?.get(i)?.tag = guestbookTopic.rank[i].id.toInt() // 글의 고유 ID를 tag에 저장
                                    topicBoard["today"]?.get(i)?.setOnClickListener {
                                        // 클릭 시 postDetail 프래그먼트로 이동
                                        val postId = it.tag as Int // tag에서 글의 고유 ID를 가져옴
                                        navigateToPostDetail(postId)
                                    }
                                }
                                else {
                                    topicBoard["today"]?.get(i)?.visibility = View.GONE
                                }
                            }
                        }
                    }
                    else {
                        Log.e("Retrofit:getTrendingGuestbook()", "notSuccessful: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<CommunityHomeTrendingGuestbook>, t: Throwable) {
                    Log.e("Retrofit:getTrendingGuestbook()", "onFailure: $t")
                }

            })
        }

        // 월간 화제글(홈) 불러오기
        fun setMonthBoard() {
            RetrofitAPI.communityHomeService.getTrendingMonth().enqueue(object: Callback<List<CommunityHomeTrendingMonthUnit>> {
                override fun onResponse(call: Call<List<CommunityHomeTrendingMonthUnit>>, response: Response<List<CommunityHomeTrendingMonthUnit>>) {
                    if(response.isSuccessful) {
                        val responseData = response.body()
                        if(responseData != null) {
                            Log.d("Retrofit:getTrendingMonth()", "Response: ${responseData}")
                            monthlyTopic = responseData
                            for(i in 0..2) {
                                if(monthlyTopic.size > i) {
                                    topicBoard["month"]?.get(i)?.visibility = View.VISIBLE
                                    topicBoard["month"]?.get(i)?.text = monthlyTopic[i].title
                                    topicBoard["month"]?.get(i)?.tag = monthlyTopic[i].id.toInt() // 글의 고유 ID를 tag에 저장
                                    topicBoard["month"]?.get(i)?.setOnClickListener {
                                        // 클릭 시 postDetail 프래그먼트로 이동
                                        val postId = it.tag as Int // tag에서 글의 고유 ID를 가져옴
                                        navigateToPostDetail(postId)
                                    }
                                }
                                else {
                                    topicBoard["month"]?.get(i)?.visibility = View.GONE
                                }
                            }
                        }
                    }
                    else {
                        Log.e("Retrofit:getTrendingMonth()", "notSuccessful: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<List<CommunityHomeTrendingMonthUnit>>, t: Throwable) {
                    Log.e("Retrofit:getTrendingMonth()", "onFailure: $t")
                }

            })
        }

        setFreeBoard() // 커뮤니티 홈 들어왔을 때 일간 화제글 불러오기
        setMonthBoard() // 커뮤니티 홈 들어왔을 때 월간 화제글 불러오기

        binding.freeBoard.setOnClickListener {
            binding.freeBoard.setTextColor(Color.parseColor("#F73D93"))
            binding.rallyCertification.setTextColor(Color.parseColor("#CFCFCF"))

            setFreeBoard()

        }
        binding.rallyCertification.setOnClickListener {
            binding.freeBoard.setTextColor(Color.parseColor("#CFCFCF"))
            binding.rallyCertification.setTextColor(Color.parseColor("#F73D93"))

            setGuestbookBoard()
        }



        binding.communityFreeBlackBoxCl.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frameLayout, CommunityFreeFragment())
                .commitAllowingStateLoss()
        }

        binding.communityRallyPinkBoxCl.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frameLayout, CommunityRallyFragment())
                .commitAllowingStateLoss()
        }

        return binding.root
    }

}