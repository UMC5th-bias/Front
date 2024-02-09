package com.example.favoriteplace

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.favoriteplace.databinding.FragmentShopBannerNewBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class ShopBannerNewFragment : Fragment() {

    lateinit var binding: FragmentShopBannerNewBinding
    private var limitedFameData = ArrayList<LimitedFame>()
    private var unlimitedFameData = ArrayList<UnlimitedFame>()

    private var limitedIconData = ArrayList<LimitedIcon>()
    private var unlimitedIconData = ArrayList<UnlimitedIcon>()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShopBannerNewBinding.inflate(inflater, container, false)

        binding.shopBannerNewIb.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frameLayout, ShopMainFragment())
                .commitAllowingStateLoss()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchLimitedSales()
        fetchUnimitedSales()
    }

    //서버에서 데이터를 가져오는 함수
    private fun fetchLimitedSales() {
        RetrofitClient.shopService.getNewLimitedSales()
            .enqueue(object : Callback<NewLimitedSalesResponse> {
                override fun onResponse(
                    call: Call<NewLimitedSalesResponse>,
                    response: Response<NewLimitedSalesResponse>
                ) {
                    if (response.isSuccessful) {
                        val newlimitedSalesResponse = response.body()

                        newlimitedSalesResponse?.let {
                            limitedFameData.clear()
                            limitedIconData.clear()

                            it.titles.forEach { status ->
                                when (status.status) {
                                    "LIMITED_SALE" -> {
                                        limitedFameData.addAll(status.itemList.map { item ->
                                            LimitedFame(item.imageUrl, item.point.toString())
                                        })
                                    }
                                }
                            }

                            it.icons.forEach { status ->
                                when (status.status) {
                                    "LIMITED_SALE" -> {
                                        limitedIconData.addAll(status.itemList.map { item ->
                                            LimitedIcon(
                                                item.imageUrl,
                                                item.point.toString(),
                                                item.name
                                            )
                                        })
                                    }
                                }
                            }
                            updateLimitedSalesUI()
                        }
                    }
                }

                override fun onFailure(call: Call<NewLimitedSalesResponse>, t: Throwable) {
                    Log.d("ShopBannerNewFragment", "Network Error: ${t.message}")
                }
            })
    }

    private fun fetchUnimitedSales() {
        RetrofitClient.shopService.getNewUnlimitedSales()
            .enqueue(object : Callback<NewUnlimitedSalesResponse> {
                override fun onResponse(
                    call: Call<NewUnlimitedSalesResponse>,
                    response: Response<NewUnlimitedSalesResponse>
                ) {
                    if (response.isSuccessful) {
                        val newunlimitedSalesResponse = response.body()

                        newunlimitedSalesResponse?.let {
                            unlimitedFameData.clear()
                            unlimitedIconData.clear()

                            it.titles.forEach { status ->
                                when (status.status) {
                                    "ALWAYS_ON_SALE" -> {
                                        unlimitedFameData.addAll(status.itemList.map { item ->
                                            UnlimitedFame(item.imageUrl, item.point.toString())
                                        })
                                    }
                                }
                            }

                            it.icons.forEach { status ->
                                when (status.status) {
                                    "ALWAYS_ON_SALE" -> {
                                        unlimitedIconData.addAll(status.itemList.map { item ->
                                            UnlimitedIcon(
                                                item.imageUrl,
                                                item.point.toString(),
                                                item.name
                                            )
                                        })
                                    }
                                }
                            }
                            updateUnlimitedSalesUI()
                        }
                    }
                }

                override fun onFailure(call: Call<NewUnlimitedSalesResponse>, t: Throwable) {
                    Log.d("ShopBannerNewFragment", "Network Error: ${t.message}")
                }

            })
    }

    private fun updateLimitedSalesUI() {
        try {
            val limitedNewFameRVAdapter = ShopBannerNewLimitedFameRVAdapter(limitedFameData)
            binding.shopBannerNewFameLimitedRv.adapter = limitedNewFameRVAdapter
            binding.shopBannerNewFameLimitedRv.layoutManager=LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
            limitedNewFameRVAdapter.setMyItemClickListener(object :ShopBannerNewLimitedFameRVAdapter.MyItemClickListener{
                override fun onItemClick() {
                    (context as MainActivity).supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frameLayout, ShopBannerLimitedFameFragment())
                        .commitAllowingStateLoss()
                }
            })

            val limitedIconRVAdapter = ShopBannerNewLimitedIconRVAdapter(limitedIconData)
            binding.shopBannerNewIconLimitedRv.adapter = limitedIconRVAdapter
            binding.shopBannerNewIconLimitedRv.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

            limitedIconRVAdapter.setMyItemClickListener(object :ShopBannerNewLimitedIconRVAdapter.MyItemClickListener{
                override fun onItemClick() {
                    (context as MainActivity).supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frameLayout, ShopBannerLimitedIconFragment())
                        .commitAllowingStateLoss()
                }
            })

        } catch (e: Exception) {
            Log.d("ShopBannerNewUpdate", "Error in update(): ${e.message}")
        }
    }
    private fun updateUnlimitedSalesUI(){
        try{
            val unlimitedFameRVAdapter=ShopBannerNewUnlimitedFameRVAdapter(unlimitedFameData)
            Log.d("LIMITEDFAME",unlimitedFameData.toString())
            binding.shopBannerNewFameUnlimitedRv.adapter=unlimitedFameRVAdapter
            binding.shopBannerNewFameUnlimitedRv.layoutManager=LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)

            unlimitedFameRVAdapter.setMyItemClickListener(object :ShopBannerNewUnlimitedFameRVAdapter.MyItemClickListener{
                override fun onItemClick() {
                    (context as MainActivity).supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frameLayout, ShopBannerUnlimitedFameFragment())
                        .commitAllowingStateLoss()
                }
            })

            val unlimitedIconRVAdapter=ShopBannerNewUnlimitedIconRVAdapter(unlimitedIconData)
            binding.shopBannerNewIconUnlimitedRv.adapter=unlimitedIconRVAdapter
            binding.shopBannerNewIconUnlimitedRv.layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)

            unlimitedIconRVAdapter.setMyItemClickListener(object :ShopBannerNewLimitedIconRVAdapter.MyItemClickListener{
                override fun onItemClick() {
                    (context as MainActivity).supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frameLayout, ShopBannerUnlimitedIconFragment())
                        .commitAllowingStateLoss()
                }
            })

        } catch (e: Exception){
            Log.d("ShopBannerNewUpdate","Error in update(): ${e.message}")
        }
    }
}
