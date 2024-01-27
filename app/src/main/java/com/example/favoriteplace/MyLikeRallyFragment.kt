package com.example.favoriteplace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.favoriteplace.databinding.FragmentMyLikeRallyBinding

class MyLikeRallyFragment : Fragment() {

    lateinit var binding: FragmentMyLikeRallyBinding
    private var myLikeRallyData=ArrayList<MyLikeRally>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentMyLikeRallyBinding.inflate(inflater,container,false)

        myLikeRallyData.apply {
            add(MyLikeRally("최애의 아이",0,4,R.drawable.animation_2))
            add(MyLikeRally("너의 이름은",0,6,R.drawable.animation_6))
            add(MyLikeRally("주술회전",0,7,R.drawable.animation_4))
        }

        val myLikeRallyRVAdapter=MyLikeRallyRVAdapter(myLikeRallyData)
        binding.myLikeRallyRv.adapter=myLikeRallyRVAdapter
        binding.myLikeRallyRv.layoutManager=LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        return binding.root
    }
}