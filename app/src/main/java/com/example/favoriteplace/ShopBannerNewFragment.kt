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
import com.google.gson.Gson
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShopBannerNewBinding.inflate(inflater, container, false)

        //돌아가기 버튼 눌렀을 때
        binding.shopBannerNewIb.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frameLayout, ShopMainFragment())
                .commitAllowingStateLoss()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchLimitedSales()  //서버에서 한정판매 데이터를 가져오는 함수
        fetchUnimitedSales()  //서버에서 상시판매 데이터를 가져오는 함수
    }

    //서버에서 한정판매 데이터를 가져오는 함수
    private fun fetchLimitedSales() {
        RetrofitClient.shopService.getNewLimitedSales()
            .enqueue(object : Callback<NewLimitedSalesResponse> {
                override fun onResponse(
                    call: Call<NewLimitedSalesResponse>,
                    response: Response<NewLimitedSalesResponse>
                ) {
                    //서버에서 데이터를 가져오는 걸 성공할 경우
                    if (response.isSuccessful) {
                        val newlimitedSalesResponse = response.body()

                        newlimitedSalesResponse?.let {
                            limitedFameData.clear()
                            limitedIconData.clear()

                            //칭호의 status가 LIMITED_SALE일 때 LimitedFame에 imageUrl, point를 저장함
                            it.titles.forEach { status ->
                                when (status.status) {
                                    "LIMITED_SALE" -> {
                                        limitedFameData.addAll(status.itemList.map { item ->
                                            Log.d("Limited_fame",item.id.toString())
                                            LimitedFame(item.imageUrl, item.point.toString(), item.id)
                                        })
                                    }
                                }
                                Log.d("LimitedFame_check",LimitedFame().toString())
                            }

                            //아이콘의 status가 LIMITED_SALE일 때 LimitedIcon에 imageUrl, point, name을 저장함
                            it.icons.forEach { status ->
                                when (status.status) {
                                    "LIMITED_SALE" -> {
                                        limitedIconData.addAll(status.itemList.map { item ->
                                            LimitedIcon(
                                                item.imageUrl,
                                                item.point.toString(),
                                                item.name,
                                                item.id
                                            )
                                        })
                                    }
                                }
                            }
                            updateLimitedSalesUI() //한정판매 데이터를 반영하여 보여주는 함수
                        }
                    }
                }

                override fun onFailure(call: Call<NewLimitedSalesResponse>, t: Throwable) {
                    Log.d("ShopBannerNewFragment", "Network Error: ${t.message}")
                }
            })
    }

    //서버에서 상시판매 데이터를 가져오는 함수
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

                            //칭호의 status가 LIMITED_SALE일 때 UnlimitedFame에 imageUrl, point를 저장함
                            it.titles.forEach { status ->
                                when (status.status) {
                                    "ALWAYS_ON_SALE" -> {
                                        unlimitedFameData.addAll(status.itemList.map { item ->
                                            UnlimitedFame(item.imageUrl, item.point.toString(), item.id)
                                        })
                                    }
                                }
                            }

                            //아이콘의 status가 LIMITED_SALE일 때 LimitedIcon에 imageUrl, point, name을 저장함
                            it.icons.forEach { status ->
                                when (status.status) {
                                    "ALWAYS_ON_SALE" -> {
                                        unlimitedIconData.addAll(status.itemList.map { item ->
                                            UnlimitedIcon(
                                                item.imageUrl,
                                                item.point.toString(),
                                                item.name,
                                                item.id
                                            )
                                        })
                                    }
                                }
                            }
                            updateUnlimitedSalesUI() //상시판매 데이터를 반영하여 보여주는 함수
                        }
                    }
                }

                override fun onFailure(call: Call<NewUnlimitedSalesResponse>, t: Throwable) {
                    Log.d("ShopBannerNewFragment", "Network Error: ${t.message}")
                }

            })
    }

    //한정판매 데이터를 반영하여 보여주는 함수
    private fun updateLimitedSalesUI() {
        try {
            //한정 판매 칭호 RVAdapter 실행
            val limitedNewFameRVAdapter = ShopBannerNewLimitedFameRVAdapter(limitedFameData)
            binding.shopBannerNewFameLimitedRv.adapter = limitedNewFameRVAdapter
            binding.shopBannerNewFameLimitedRv.layoutManager=LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)

            //한정 판매 칭호 아이템을 눌렀을 때 상세페이지로 넘어감
            limitedNewFameRVAdapter.setMyItemClickListener(object :ShopBannerNewLimitedFameRVAdapter.MyItemClickListener{
                override fun onItemLimitedFameClick(limitedFame: LimitedFame) {
                    (context as MainActivity).supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frameLayout, ShopBannerLimitedFameFragment().apply {

                            //아이템 아이디를 신상품 페이지 한정 칭호에게 gson으로 보내주는 코드
                            arguments=Bundle().apply {
                                val gson=Gson()
                                val limitedFameJson=gson.toJson(limitedFame.id)
                                putString("limitedFame",limitedFameJson)
                            }
                        })
                        .commitAllowingStateLoss()
                }
            })

            //한정 판매 아이콘 RVAdapter 실행
            val limitedIconRVAdapter = ShopBannerNewLimitedIconRVAdapter(limitedIconData)
            binding.shopBannerNewIconLimitedRv.adapter = limitedIconRVAdapter
            binding.shopBannerNewIconLimitedRv.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

            //한정 판매 아이콘 아이템을 눌렀을 때 상세페이지로 넘어감
            limitedIconRVAdapter.setMyItemClickListener(object :ShopBannerNewLimitedIconRVAdapter.MyItemClickListener{
                override fun onItemLimitedIconClick() {
                    (context as MainActivity).supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frameLayout, ShopBannerLimitedIconFragment())
                        .commitAllowingStateLoss()
                }
            })

        } catch (e: Exception) {
            Log.d("ShopBannerNewUpdate", "Error in update(): ${e.message}")
        }
    }

    //상시 판매 데이터를 반영하여 보여주는 함수
    private fun updateUnlimitedSalesUI(){
        try{
            //상시 판매 칭호 RVAdapter 실행
            val unlimitedFameRVAdapter=ShopBannerNewUnlimitedFameRVAdapter(unlimitedFameData)
            binding.shopBannerNewFameUnlimitedRv.adapter=unlimitedFameRVAdapter
            binding.shopBannerNewFameUnlimitedRv.layoutManager=LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)

            //상시 판매 칭호 아이템을 눌렀을 때 상세페이지로 넘어감
            unlimitedFameRVAdapter.setMyItemClickListener(object :ShopBannerNewUnlimitedFameRVAdapter.MyItemClickListener{
                override fun onItemUnlimitedFameClick() {
//                    changeLimitedFameFragment(LimitedFame())
//                    (context as MainActivity).supportFragmentManager.beginTransaction()
//                        .replace(R.id.main_frameLayout, ShopBannerUnlimitedFameFragment())
//                        .commitAllowingStateLoss()
                }
            })

            //상시 판매 아이콘 RVAdapter 실행
            val unlimitedIconRVAdapter=ShopBannerNewUnlimitedIconRVAdapter(unlimitedIconData)
            binding.shopBannerNewIconUnlimitedRv.adapter=unlimitedIconRVAdapter
            binding.shopBannerNewIconUnlimitedRv.layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)

            //상시 판매 아이콘 아이템을 눌렀을 때 상세페이지로 넘어감
            unlimitedIconRVAdapter.setMyItemClickListener(object :ShopBannerNewUnlimitedIconRVAdapter.MyItemClickListener{
                override fun onItemUnlimitedIconClick() {
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
