package com.example.favoriteplace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.favoriteplace.databinding.FragmentCommunityRallyCommendBinding

class CommunityRallyCommendFragment : Fragment() {

    lateinit var binding: FragmentCommunityRallyCommendBinding
    private var rallyCommendData=ArrayList<RallyCommend>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentCommunityRallyCommendBinding.inflate(inflater,container,false)

        //더미데이터로 데이터 삽입. 서버에서 가져올 경우 삭제할 코드
        rallyCommendData.apply{
            add(RallyCommend("2024.01.05","17:00","저도 이번 6월에 도쿄타워 다녀왔는데 날씨도 좋고..","8월 말 도쿄타워 투어하고 주변 다니..","남도일",3,52,"13:08",3))
            add(RallyCommend("2024.01.07","13:20","최애의 아이 성지순례라니..!!","최애의 아이 스크램블 교차로 다녀 ..","내이름은코난",3,27,"12:58",8))
        }
        //

        val commendRVAdapter=CommunityRallyCommendRVAdapter(rallyCommendData)
        binding.communityRallyCommendRv.adapter=commendRVAdapter
        binding.communityRallyCommendRv.layoutManager=
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        return binding.root
    }
}