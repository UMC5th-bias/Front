package com.example.favoriteplace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.favoriteplace.databinding.FragmentMyVerifyRallyBinding

class MyVerifyRallyFragment : Fragment() {

    lateinit var binding: FragmentMyVerifyRallyBinding
    private var myVerifyRallyData=ArrayList<MyVerifyRally>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentMyVerifyRallyBinding.inflate(inflater,container,false)

        myVerifyRallyData.apply {
            add(MyVerifyRally("최애의 아이",0,4,R.drawable.animation_2))
            add(MyVerifyRally("날씨의 아이",0,6,R.drawable.animation_1))
        }

        val myVerifyRallyRVAdapter=MyVerifyRallyRVAdapter(myVerifyRallyData)
        binding.myVerifyRallyRv.adapter=myVerifyRallyRVAdapter
        binding.myVerifyRallyRv.layoutManager=
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        return binding.root
    }
}