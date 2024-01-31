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
    private var limitedIconData=ArrayList<LimitedIcon>()
    private var unlimitedIconData=ArrayList<UnlimitedIcon>()

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

        limitedIconData.apply {
            add(LimitedIcon(R.drawable.limited_icon_1,"유닝이","20000P"))
            add(LimitedIcon(R.drawable.limited_icon_1,"Developer","10000P"))
            add(LimitedIcon(R.drawable.limited_icon_1,"유닝이","20000P"))
        }

        unlimitedIconData.apply {
            add(UnlimitedIcon(R.drawable.unlimited_icon_1,"별행성","10000P"))
            add(UnlimitedIcon(R.drawable.unlimited_icon_2,"새턴","10000P"))
            add(UnlimitedIcon(R.drawable.unlimited_icon_3,"초승달","10000P"))
        }

        val limitedFameRVAdapter=ShopBannerNewLimitedFameRVAdapter(limitedFameData)
        binding.shopBannerNewFameLimitedRv.adapter=limitedFameRVAdapter
        binding.shopBannerNewFameLimitedRv.layoutManager=LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        limitedFameRVAdapter.setMyItemClickListener(object :ShopBannerNewLimitedFameRVAdapter.MyItemClickListener{
            override fun onItemClick() {
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frameLayout, ShopBannerLimitedFameFragment())
                    .commitAllowingStateLoss()
            }
        })

        val unlimitedFameRVAdapter=ShopBannerNewUnlimitedFameRVAdapter(unlimitedFameData)
        binding.shopBannerNewFameUnlimitedRv.adapter=unlimitedFameRVAdapter
        binding.shopBannerNewFameUnlimitedRv.layoutManager=LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        unlimitedFameRVAdapter.setMyItemClickListener(object :ShopBannerNewUnlimitedFameRVAdapter.MyItemClickListener{
            override fun onItemClick() {
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frameLayout, ShopBannerUnlimitedFameFragment())
                    .commitAllowingStateLoss()
            }
        })

        val limitedIconRVAdapter=ShopBannerNewLimitedIconRVAdapter(limitedIconData)
        binding.shopBannerNewIconLimitedRv.adapter=limitedIconRVAdapter
        binding.shopBannerNewIconLimitedRv.layoutManager=LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        limitedIconRVAdapter.setMyItemClickListener(object :ShopBannerNewLimitedIconRVAdapter.MyItemClickListener{
            override fun onItemClick() {
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frameLayout, ShopBannerLimitedIconFragment())
                    .commitAllowingStateLoss()
            }
        })

        val unlimitedIconRVAdapter=ShopBannerNewUnlimitedIconRVAdapter(unlimitedIconData)
        binding.shopBannerNewIconUnlimitedRv.adapter=unlimitedIconRVAdapter
        binding.shopBannerNewIconUnlimitedRv.layoutManager=LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        return binding.root
    }
}