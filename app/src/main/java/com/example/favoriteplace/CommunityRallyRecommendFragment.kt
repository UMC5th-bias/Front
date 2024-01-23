package com.example.favoriteplace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.favoriteplace.databinding.FragmentCommunityRallyLatelyBinding

class CommunityRallyRecommendFragment: Fragment() {

    lateinit var binding: FragmentCommunityRallyLatelyBinding
    private var rallyRecommendWriteData=ArrayList<RallyRecommend>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentCommunityRallyLatelyBinding.inflate(inflater,container,false)

        rallyRecommendWriteData.apply {
            add(RallyRecommend(R.drawable.community_rally_place_5,5,"8월 말 도쿄타워 투어하고 주변 구경..","미츠하","#너의이름은","#도쿄",70,52,"2일전",3))
            add(RallyRecommend(R.drawable.community_rally_place_6,5,"날씨의 아이 성지순례 언제 가는게 ..","이즈리얼","#날씨의아이","#도쿄",66,33,"3일전",10))
            add(RallyRecommend(R.drawable.community_rally_place_7,5,"최애의 아이 스크램블 교차로 다녀 ..","키라키라","#최애의아이","#도쿄",50,27,"3일전",8))
            add(RallyRecommend(R.drawable.community_rally_place_8,5,"시부야 사변 갔다가, 하라주쿠 팝업 ..","ㅇㅅㅇ","#최애의아이","#도쿄",48,18,"2일전",5))

        }

        val rallyRecommendRVAdapter=CommunityRallyRecommendRVAdapter(rallyRecommendWriteData)
        binding.communityRallyLatelyRv.adapter=rallyRecommendRVAdapter
        binding.communityRallyLatelyRv.layoutManager=
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        return binding.root

    }
}