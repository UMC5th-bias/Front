package com.example.favoriteplace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.favoriteplace.databinding.FragmentCommunityFreeRecommendBinding

class CommunityFreeRecommendFragment : Fragment() {

    lateinit var binding: FragmentCommunityFreeRecommendBinding
    private var freeRecommendWriteData = ArrayList<FreeRecommend>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentCommunityFreeRecommendBinding.inflate(inflater,container,false)

        //더미데이터로 데이터 삽입. 서버에서 가져올 경우 삭제할 코드
        freeRecommendWriteData.apply {
            add(FreeRecommend("나고야 주변 성지순레 리스트 모음집.zip","내이름은코난",3,58,"20:10",10))
            add(FreeRecommend("2024 새해 이 사진 보면 돈 들어옴 공감 눌러주세","키미노나마에와",4,55,"20:08",7))
            add(FreeRecommend("도쿄 주변 맛집 & 성지순례 추천 해주세요!","로이도삼",12,42,"19:07",8))
            add(FreeRecommend("흑발 vs 금발 투표 ㄱㄱㄱ","알칼로이드",7,35,"19:00",3))
            add(FreeRecommend("가볍게 볼 애니 추천해주실 분?","치아키상",20,22,"15:59",10))
            add(FreeRecommend("저녁 메뉴 추천 받아요~!","일이삼",10,19,"13:50",2))
            add(FreeRecommend("방학 하니까 살 것 같다 ..","빵",15,15,"12:08",3))
            add(FreeRecommend("죄송합니다 교수님..근데 교수님도 저한","에이쁠",15,11,"10:30",0))
            add(FreeRecommend("새해 복 많이 받어 다들 !!","빵",15,10,"10:15",5))
        }
        //

        val recommendwriteRVAdapter=CommunityFreeRecommendRVAdapter(freeRecommendWriteData)
        binding.communityFreeRecommendRv.adapter=recommendwriteRVAdapter
        binding.communityFreeRecommendRv.layoutManager=LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        return binding.root
    }
}