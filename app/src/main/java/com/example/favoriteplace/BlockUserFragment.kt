package com.example.favoriteplace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.favoriteplace.databinding.FragmentBlockUserBinding

class BlockUserFragment : Fragment() {

    lateinit var binding: FragmentBlockUserBinding
    private var blockUserData=ArrayList<BlockUser>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentBlockUserBinding.inflate(inflater,container,false)


        blockUserData.apply{
            add(BlockUser(R.drawable.memberimg,R.drawable.ic_shop1,R.drawable.ic_shoptitle,"난나나"))
            add(BlockUser(R.drawable.memberimg,R.drawable.ic_shop3,R.drawable.ic_shoptitle,"가각"))
            add(BlockUser(R.drawable.memberimg,R.drawable.ic_shop2,R.drawable.ic_shoptitle,"난나나"))
        }

        val BlockUserRVAdapter=BlockUserRVAdapter(blockUserData)
        binding.blockUserUserRv.adapter=BlockUserRVAdapter
        binding.blockUserUserRv.layoutManager=
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


        return binding.root
    }
}