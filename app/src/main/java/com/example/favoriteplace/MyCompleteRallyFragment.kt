package com.example.favoriteplace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.favoriteplace.databinding.FragmentMyCompleteRallyBinding
import com.example.favoriteplace.databinding.FragmentMyLikeRallyBinding

class MyCompleteRallyFragment : Fragment() {

    lateinit var binding: FragmentMyCompleteRallyBinding
    private var myCompleteRallyData=ArrayList<MyCompleteRally>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentMyCompleteRallyBinding.inflate(inflater,container,false)

        myCompleteRallyData.apply {
            add(MyCompleteRally("최애의 아이",0,4,R.drawable.animation_2))
        }

        val myCompleteRallyRVAdapter=MyCompleteRallyRVAdapter(myCompleteRallyData)
        binding.myCompleteRallyRv.adapter=myCompleteRallyRVAdapter
        binding.myCompleteRallyRv.layoutManager=
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        return binding.root
    }
}