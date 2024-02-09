package com.example.favoriteplace

import android.annotation.SuppressLint
import android.content.Context
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

class ShopBannerNewFragment : Fragment() {

    lateinit var binding: FragmentShopBannerNewBinding
    private var limitedFameData=ArrayList<LimitedFame>()
    private var unlimitedFameData=ArrayList<UnlimitedFame>()
    private var limitedIconData=ArrayList<LimitedIcon>()
    private var unlimitedIconData=ArrayList<UnlimitedIcon>()

//    private var shopData=ArrayList<ShopData>()
//    private var titlesItemList=ArrayList<TitlesItemList>()
//    private var titlesData=ArrayList<TitlesData>()
//
//    var limitedFameRVAdapter=ShopBannerNewLimitedFameRVAdapter(shopData)

    override fun onAttach(context: Context) {
        super.onAttach(context)
//        getNewItem()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentShopBannerNewBinding.inflate(inflater,container,false)

//        getNewItem()

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

//        limitedFameData.apply {
//            add(LimitedFame(null, 3000))
//        }

//        limitedFameRVAdapter.setMyItemClickListener(object :ShopBannerNewLimitedFameRVAdapter.MyItemClickListener{
//            override fun onItemClick() {
//                (context as MainActivity).supportFragmentManager.beginTransaction()
//                    .replace(R.id.main_frameLayout, ShopBannerLimitedFameFragment())
//                    .commitAllowingStateLoss()
//            }
//        })

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

//        limitedFameRVAdapter.notifyDataSetChanged()

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

    //서버에서 데이터를 가져오는 함수
//    private fun getNewItem(){
//        val authService= getRetrofit().create(AuthRetrofitInterface::class.java)
//        authService.getNewItem().enqueue(object: Callback<ShopData> {
//
//            @SuppressLint("NotifyDataSetChanged")
//            override fun onResponse(call: Call<ShopData>, response: Response<ShopData>) {
////                val JSON_data=response.body()
////                Log.d("API 연동 JSON_data", JSON_data.toString())
////                val icons_data= response.body()?.icons?.get(0)?.
////                Log.d("API 연동 ICONS_DATA",icons_data.toString())
////                Log.d("API 연동 ICONS_DATA",icons_data?.get(0).toString())
////                val titles_data=response.body()?.titles
////                Log.d("API 연동 TITLES_DATA", titles_data.toString())
//                val getTitle=response.body()?.titles
//                val getIcon=response.body()?.icons
//                val b=getTitle?.get(0)?.itemList?.size
//
////                if(getTitle?.get(0)?.status=="LIMITED_SALE"){
//                val titlesSize= getTitle?.get(0)?.itemList?.size
//
//                for (i in 0 until titlesSize!!){
//                    Log.d("칭호 아이디", getTitle[0].itemList[i].id.toString())
//                    Log.d("칭호 이미지", getTitle[0].itemList[i].imageUrl.toString())
//                    Log.d("칭호 포인트", getTitle[0].itemList[i].point.toString())
//
//                    titlesItemList.apply{
//                        add(TitlesItemList(getTitle[0].itemList[i].id,"",getTitle[0].itemList[i].imageUrl,getTitle[0].itemList[i].point))
//                    }
//                    titlesData.apply {
//                        add(TitlesData(getTitle[0].status,titlesItemList))
//                    }
//
//                    shopData.apply {
//                        add(ShopData(titlesData,null))
//                    }
//
////                    limitedFameRVAdapter.addLimitedFame(ArrayList(shopData))
//
//                    binding.shopBannerNewFameLimitedRv.adapter=limitedFameRVAdapter
//                    binding.shopBannerNewFameLimitedRv.layoutManager=LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//
//
//                    Log.d("limitedFame",shopData.toString())
//                }
////                }
//                if(getTitle[1].status=="ALWAYS_ON_SALE"){
//                    val titlesSize= getTitle[1].itemList.size
//                    for (i in 0 until titlesSize){
////                        Log.d("칭호 아이디", getTitle[1].itemList[i].id.toString())
////                        Log.d("칭호 이미지", getTitle[1].itemList[i].imageUrl)
////                        Log.d("칭호 포인트", getTitle[1].itemList[i].point.toString())
//                    }
//                }
////                Log.d("칭호",getTitle.toString())
////
////                Log.d("상시칭호",getTitle?.get(1)?.status.toString())
////                Log.d("칭호 내용",getTitle?.get(1)?.itemList.toString())
////                Log.d("칭호 아이디",getTitle?.get(1)?.itemList?.get(1)?.id.toString())
////                Log.d("칭호 이름",getTitle?.get(1)?.itemList?.get(1)?.name.toString())
////                Log.d("칭호 이미지",getTitle?.get(1)?.itemList?.get(1)?.imageUrl.toString())
////                Log.d("칭호 포인트",getTitle?.get(1)?.itemList?.get(1)?.point.toString())
//            }
//
//            override fun onFailure(call: Call<ShopData>, t: Throwable) {
//                Log.d("API 연동", t.message.toString())
//            }
//
//        })
//    }


}