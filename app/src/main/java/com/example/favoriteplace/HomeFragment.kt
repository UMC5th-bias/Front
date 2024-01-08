package com.example.favoriteplace


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.collection.arraySetOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.favoriteplace.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding

    private var homeItemDatas = ArrayList<HomeItem>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)


        homeItemDatas.apply {
            add(HomeItem(R.drawable.item1,"여기짱재밋어요하하하하하하하","#날씨의아이","#도쿄","1시간 전","성지순례 인증"))
            add(HomeItem(R.drawable.item1,"나고야 주변 성지순례","#날씨의아이","#너의이름은","1시간 전","자유게시판"))
            add(HomeItem(R.drawable.item1,"날씨의아이어쩌구","#날씨의아이"," ","1시간 전","성지순례 인증"))
            add(HomeItem(R.drawable.item1,"토토로보고 왔어요오오~","#이웃집 토토로"," ","1시간 전","성지순례 인증"))
        }


        val homeItemRVAdapter = HomeItemRVAdapter(homeItemDatas)
        binding.recyclerView.adapter=homeItemRVAdapter
        binding.recyclerView.layoutManager=LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL,false)




        return binding.root

    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        // gif 배너
//        val homeBanner = view.findViewById<ImageView>(R.id.home_banner_cv)
//        Glide.with(this).asGif().load(R.raw.banner1).into(homeBanner)
//    }



}