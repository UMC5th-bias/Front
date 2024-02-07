package com.example.favoriteplace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.favoriteplace.databinding.FragmentRallycategoryBinding

class RallyCategoryFragment : Fragment() {

    lateinit var binding: FragmentRallycategoryBinding
    private var animationDatas = ArrayList<Animation>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentRallycategoryBinding.inflate(inflater,container,false)

        //더미데이터로 데이터 삽입. 서버에서 가져올 경우 삭제할 코드
        animationDatas.apply {
            add(Animation(R.drawable.animation_1, "날씨의 아이","0/6"))
            add(Animation(R.drawable.animation_2, "최애의 아이","0/10"))
            add(Animation(R.drawable.animation_3, "너의 이름은","0/20"))
            add(Animation(R.drawable.animation_4, "주술회전","0/8"))
            add(Animation(R.drawable.animation_5, "러브라이브","0/10"))
        }
        //

        val animationRVAdapter=AnimationRVAdapter(animationDatas, context as MainActivity)
        binding.rallyCategoryAnimationRv.adapter=animationRVAdapter
        binding.rallyCategoryAnimationRv.layoutManager=LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        return binding.root
    }
}