package com.example.favoriteplace

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.favoriteplace.databinding.FragmentShopBannerNewBinding
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import retrofit2.http.Url

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

        getNewItem()

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

        unlimitedIconRVAdapter.setMyItemClickListener(object :ShopBannerNewLimitedIconRVAdapter.MyItemClickListener{
            override fun onItemClick() {
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frameLayout, ShopBannerUnlimitedIconFragment())
                    .commitAllowingStateLoss()
            }
        })

        binding.shopBannerNewIb.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frameLayout, ShopMainFragment())
                .commitAllowingStateLoss()
        }

        return binding.root
    }

    //서버에서 받을 데이터 클래스
    data class JSON_data(
        @SerializedName("titles") val titles: List<titles_data>,
        @SerializedName("icons") val icons: List<icons_data>
    )

    data class titles_data(
        @SerializedName("status") val status: String,
        @SerializedName("itemList") val itemList: List<titles_itemList>
    )

    data class titles_itemList(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name:String,
        @SerializedName("imageUrl") val imageUrl: String,
        @SerializedName("point") val point:Int
    )

    data class icons_data(
        @SerializedName("status") val status: String,
        @SerializedName("itemList") val itemList: List<icons_itemList>
    )

    data class icons_itemList(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name:String,
        @SerializedName("imageUrl") val imageUrl: String,
        @SerializedName("point") val point:Int
    )

    //서버에서 데이터를 가져오는 함수
    private fun getNewItem(){
        val authService= getRetrofit().create(AuthRetrofitInterface::class.java)
        authService.getNewItem().enqueue(object: Callback<JSON_data>{

            override fun onResponse(call: Call<JSON_data>, response: Response<JSON_data>) {
                Log.d("API 연동","SUCCESS")
            }

            override fun onFailure(call: Call<JSON_data>, t: Throwable) {
                Log.d("API 연동", t.message.toString())
            }

        })
    }


}
