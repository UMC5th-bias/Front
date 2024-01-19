package com.example.favoriteplace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.favoriteplace.databinding.FragmentShopBannerNewBinding

class ShopBannerNewFragment : Fragment() {

    lateinit var binding: FragmentShopBannerNewBinding
    private var limitedFameData=ArrayList<LimitedFame>()
    private var unlimitedFameData=ArrayList<UnlimitedFame>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentShopBannerNewBinding.inflate(inflater,container,false)

        limitedFameData.apply{
            add(LimitedFame(R.drawable.limited_fame_1,"5000P"))
            add(LimitedFame(R.drawable.limited_fame_2,"30000P"))
            add(LimitedFame(R.drawable.limited_fame_3,"300000P"))
        }

        unlimitedFameData.apply {
            add(UnlimitedFame(R.drawable.unlimited_fame_1, "5000P"))
            add(UnlimitedFame(R.drawable.unlimited_fame_2, "10000P"))
            add(UnlimitedFame(R.drawable.unlimited_fame_3, "100000P"))
        }

        val limitedFameRVAdapter=ShopBannerNewLimitedFameRVAdapter(limitedFameData)
        binding.shopBannerNewFameLimitedRv.adapter=limitedFameRVAdapter
        binding.shopBannerNewFameLimitedRv.layoutManager=LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val unlimitedFameRVAdapter=ShopBannerVewUnlimitedFameRVAdapter(unlimitedFameData)
        binding.shopBannerNewFameUnlimitedRv.adapter=unlimitedFameRVAdapter
        binding.shopBannerNewFameUnlimitedRv.layoutManager=LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        return binding.root
    }
}