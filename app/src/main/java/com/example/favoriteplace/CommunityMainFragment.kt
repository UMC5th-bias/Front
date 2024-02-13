package com.example.favoriteplace

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

        val monthlyTopic: List<String> = listOf(
            "나고야 주변 성지순례 리스트 모음집.zip",
            "날씨의 아이 성지순례 언제 가는게 좋을지 추천 해주세요!",
            "8월 말 도쿄타원 투어하고 주변 다니면서 성지순례 한 후기"
        )

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

        setFreeBoard() // 커뮤니티 홈 들어왔을 때 화제글 보여주기

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

        for(i in 0..2) {
            if(monthlyTopic.size > i) {
                topicBoard["month"]?.get(i)?.visibility = View.VISIBLE
                topicBoard["month"]?.get(i)?.text = monthlyTopic[i]
            }
            else {
                topicBoard["month"]?.get(i)?.visibility = View.GONE
            }
        }

        binding.communityFreeIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frameLayout, CommunityFreeFragment())
                .commitAllowingStateLoss()
        }

        binding.communityRallyIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frameLayout, CommunityRallyFragment())
                .commitAllowingStateLoss()
        }

        return binding.root
    }

}