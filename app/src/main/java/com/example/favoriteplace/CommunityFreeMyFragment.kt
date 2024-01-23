package com.example.favoriteplace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.favoriteplace.databinding.FragmentCommunityFreeMyBinding
import com.example.favoriteplace.databinding.FragmentCommunityFreeRecommendBinding

class CommunityFreeMyFragment : Fragment() {

    lateinit var binding: FragmentCommunityFreeMyBinding
    private var freeMyWriteData = ArrayList<FreeMyWrite>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentCommunityFreeMyBinding.inflate(inflater,container,false)

        //더미데이터로 데이터 삽입. 서버에서 가져올 경우 삭제할 코드
        freeMyWriteData.apply {
            add(FreeMyWrite("도쿄 성지순례 장소 추천해주세요:)","아이",3,0,"13:10",1))
            add(FreeMyWrite("2024 새해 좋은 일만 가득하세요","아이",4,1,"13:08",1))
            add(FreeMyWrite("심심한데 뭐하지","아이",12,3,"13:07",1))
        }
        //

        val mywriteRVAdapter=CommunityFreeMyRVAdapter(freeMyWriteData)
        binding.communityFreeMyRv.adapter=mywriteRVAdapter
        binding.communityFreeMyRv.layoutManager=LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        return binding.root
    }
}