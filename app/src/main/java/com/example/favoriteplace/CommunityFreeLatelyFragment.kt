package com.example.favoriteplace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.favoriteplace.databinding.FragmentCommunityFreeLatelyBinding

class CommunityFreeLatelyFragment : Fragment() {

    lateinit var binding: FragmentCommunityFreeLatelyBinding
    private var latelyWriteDatas = ArrayList<LatelyWrite>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentCommunityFreeLatelyBinding.inflate(inflater,container,false)

        //더미데이터로 데이터 삽입. 서버에서 가져올 경우 삭제할 코드
        latelyWriteDatas.apply {
            add(LatelyWrite("코난 성지순례 가보신 분?","내이름은코난",3,3,"13:10",2))
            add(LatelyWrite("재밌는 애니 추천 좀 해주세요.","ㅇㅅㅇ",4,1,"13:08",1))
            add(LatelyWrite("일본 오사카 vs 후쿠오카 어디갈까요","로이도삼",12,3,"13:07",1))
            add(LatelyWrite("심심하다ㅏㅏㅏ","넝담검",7,5,"13:00",0))
            add(LatelyWrite("제 MBTI 맞춰보세요.","키라",20,1,"12:59",5))
            add(LatelyWrite("저녁 메뉴 추천 받아요~!","빵",10,1,"12:50",2))
            add(LatelyWrite("방학 하니까 살 것 같다 ..","집이 최고",15,15,"12:48",3))
            add(LatelyWrite("총신대 주변 카공하기 좋은 곳 아는 사람!","하치와레",15,5,"12:48",0))
            add(LatelyWrite("유튜브도 볼 것 없다 ..","룹이",15,5,"12:48",0))
            add(LatelyWrite("코난 성지순례 가보신 분?","내이름은코난",3,3,"13:10",2))
            add(LatelyWrite("재밌는 애니 추천 좀 해주세요.","ㅇㅅㅇ",4,1,"13:08",1))
            add(LatelyWrite("일본 오사카 vs 후쿠오카 어디갈까요","로이도삼",12,3,"13:07",1))
            add(LatelyWrite("심심하다ㅏㅏㅏ","넝담검",7,5,"13:00",0))
            add(LatelyWrite("제 MBTI 맞춰보세요.","키라",20,1,"12:59",5))
            add(LatelyWrite("저녁 메뉴 추천 받아요~!","빵",10,1,"12:50",2))
            add(LatelyWrite("방학 하니까 살 것 같다 ..","집이 최고",15,15,"12:48",3))
            add(LatelyWrite("총신대 주변 카공하기 좋은 곳 아는 사람!","하치와레",15,5,"12:48",0))
            add(LatelyWrite("유튜브도 볼 것 없다 ..","룹이",15,5,"12:48",0))
        }
        //

        val latelywriteRVAdapter=CommunityFreeLatelyRVAdapter(latelyWriteDatas)
        binding.communityFreeLatelyRv.adapter=latelywriteRVAdapter
        binding.communityFreeLatelyRv.layoutManager=LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        return binding.root
    }
}