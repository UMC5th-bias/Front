package com.example.favoriteplace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.favoriteplace.databinding.FragmentCommunityRallyLatelyBinding

class CommunityRallyLatelyFragment : Fragment() {

    lateinit var binding: FragmentCommunityRallyLatelyBinding
    private var rallyLatelyWriteData=ArrayList<RallyLatelyWrite>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentCommunityRallyLatelyBinding.inflate(inflater,container,false)

        rallyLatelyWriteData.apply {
            add(RallyLatelyWrite(R.drawable.community_rally_place_1,5,"도쿄타워 #러브라이브 성지순례!","zi존이다","#러브라이브","#도쿄",3,0,"5분전",0))
            add(RallyLatelyWrite(R.drawable.community_rally_place_2,5,"시부야 스크램블 교차로 사람 정말 ..","키라키라","#최애의아이","#도쿄",3,0,"20분전",8))
            add(RallyLatelyWrite(R.drawable.community_rally_place_3,5,"오다이바 해변공원 날씨가 좋지는 않","키미노 나마에와","#날씨의아이","#도쿄",3,0,"30분전",0))
            add(RallyLatelyWrite(R.drawable.community_rally_place_4,5,"주술회전 하라주쿠역 영접하고 왔습..","고죠사마","#주술회전","#도쿄",3,0,"1시간전",0))

        }

        val rallyLatelyRVAdapter=CommunityRallyLatelyRVAdapter(rallyLatelyWriteData)
        binding.communityRallyLatelyRv.adapter=rallyLatelyRVAdapter
        binding.communityRallyLatelyRv.layoutManager=LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        return binding.root

    }
}