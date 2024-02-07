package com.example.favoriteplace

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.favoriteplace.databinding.FragmentCommunityMainBinding

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

        val todayTopic: Map<String, List<String>> = mapOf(
            "자유게시판" to listOf(
                "나고야 주변 성지순례 리스트 모음집.zip",
                "2024 새해 이 사진 보면 돈 들어옴 공감 눌러주세요!",
                "도쿄 주변 맛집 & 성지순례 추천 해주세요!",
                "흑발 vs 금발 투표 ㄱㄱㄱ",
                "가볍게 볼 애니 추천 해주실 분 ?"
            ),
            "성지순례인증" to listOf(
                "8월 말 도쿄타워 투어하고 주변 구경했어요.",
                "날씨의아이 성지순례 언제 가는게 좋은지 추천 해주세요!",
                "최애의 아이 스크램블 교차로 다녀왔어요!",
                "시부야 사변 갔다가, 하라주쿠 팝업까지 다녀옴",
                "러브라이브 도쿄 결승 대회장인 진구 경기장 다녀왔습니다."

            )
        )

        val monthlyTopic: List<String> = listOf(
            "나고야 주변 성지순례 리스트 모음집.zip",
            "날씨의 아이 성지순례 언제 가는게 좋을지 추천 해주세요!",
            "8월 말 도쿄타원 투어하고 주변 다니면서 성지순례 한 후기"
        )

        binding.freeBoard.setOnClickListener {
            binding.freeBoard.setTextColor(Color.parseColor("#F73D93"))
            binding.rallyCertification.setTextColor(Color.parseColor("#CFCFCF"))
            for(i in 0..4) {
                if((todayTopic["자유게시판"]?.size ?: 0) > i) {
                    topicBoard["today"]?.get(i)?.visibility = View.VISIBLE
                    topicBoard["today"]?.get(i)?.text = todayTopic["자유게시판"]?.get(i)
                }
                else {
                    topicBoard["today"]?.get(i)?.visibility = View.GONE
                }
            }
        }
        binding.rallyCertification.setOnClickListener {
            binding.freeBoard.setTextColor(Color.parseColor("#CFCFCF"))
            binding.rallyCertification.setTextColor(Color.parseColor("#F73D93"))
            for(i in 0..4) {
                if((todayTopic["성지순례인증"]?.size ?: 0) > i) {
                    topicBoard["today"]?.get(i)?.visibility = View.VISIBLE
                    topicBoard["today"]?.get(i)?.text = todayTopic["성지순례인증"]?.get(i)
                }
                else {
                    topicBoard["today"]?.get(i)?.visibility = View.GONE
                }
            }
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