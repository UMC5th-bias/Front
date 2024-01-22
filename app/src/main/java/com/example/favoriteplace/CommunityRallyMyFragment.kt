package com.example.favoriteplace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.favoriteplace.databinding.FragmentCommunityRallyMyBinding

class CommunityRallyMyFragment : Fragment() {

    lateinit var binding: FragmentCommunityRallyMyBinding
    private var rallyMyWriteData=ArrayList<RallyMyWrite>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentCommunityRallyMyBinding.inflate(inflater,container,false)

        //더미데이터로 데이터 삽입. 서버에서 가져올 경우 삭제할 코드
        rallyMyWriteData.apply {
            add(RallyMyWrite("날씨의 아이 덕후 투어 다녀왔어요:)", "아이", 3, 0, "13:10", 3))
        }
        //

        val mywriteRVAdapter=CommunityRallyMyRVAdapter(rallyMyWriteData)
        binding.communityRallyMyRv.adapter=mywriteRVAdapter
        binding.communityRallyMyRv.layoutManager=
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        return binding.root
    }
}