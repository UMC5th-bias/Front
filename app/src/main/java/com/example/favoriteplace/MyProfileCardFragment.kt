package com.example.favoriteplace

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import com.example.favoriteplace.databinding.FragmentMyProfileCardBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyProfileCardFragment : Fragment() {
    lateinit var binding : FragmentMyProfileCardBinding
    private var limitedFameData=ArrayList<LimitedFame>()
    private var unlimitedFameData=ArrayList<UnlimitedFame>()
    private var limitedIconData=ArrayList<LimitedIcon>()
    private var unlimitedIconData=ArrayList<UnlimitedIcon>()
    private var userToken: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyProfileCardBinding.inflate(inflater, container, false)

        checkLoginStatus(requireActivity()) // 인증 정보 가져오기

        getMyProfile(requireActivity()) //내 프로필 정보 가져오기

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        // 초기 상태 설정
        binding.myProfileCardSc.isChecked = false
        binding.limitedSaleContainer.visibility = View.VISIBLE
        binding.regularSaleContainer.visibility = View.GONE

        badge()


        binding.myProfileCardSwitchLimitedTv.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )
        binding.myProfileCardSwitchRegularTv.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.black
            )
        )

        // 상태 변경 리스너 설정
        binding.myProfileCardSc.setOnCheckedChangeListener { _, isChecked ->
            Handler(Looper.getMainLooper()).postDelayed({
                if (isChecked) {
                    binding.limitedSaleContainer.visibility = View.GONE
                    binding.regularSaleContainer.visibility = View.VISIBLE
                    binding.myProfileCardSwitchLimitedTv.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.black
                        )
                    )
                    binding.myProfileCardSwitchRegularTv.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                    icon()

                } else {
                    binding.limitedSaleContainer.visibility = View.VISIBLE
                    binding.regularSaleContainer.visibility = View.GONE
                    binding.myProfileCardSwitchLimitedTv.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                    binding.myProfileCardSwitchRegularTv.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.black
                        )
                    )

                    badge()
                }
            }, 100) // 100ms 지연
        }
    }


    private fun icon() {

        getMyItems("icon") //아이콘 불러오기

    }


    private fun badge() {



        getMyItems("title") //칭호 불러오기

    }

    fun getMyItems(type: String) {
        //내 프로필 정보 불러오기
        RetrofitAPI.myService.getMyitems(authorization = "Bearer $userToken", type = type).enqueue(object: Callback<MyItems> {
            override fun onResponse(call: Call<MyItems>, response: Response<MyItems>) {
                if(response.isSuccessful) {
                    val responseData = response.body()
                    if(responseData != null) {
                        Log.d("getMyitems()", "Type: $type, Response: ${responseData}")
                        if(type == "icon") { // 아이콘
                            limitedIconData = ArrayList()
                            unlimitedIconData = ArrayList()
                            responseData.limited.forEach {
                                limitedIconData.add(LimitedIcon(it.imageUrl, "", "", it.id.toInt()))
                            }
                            responseData.always.forEach {
                                unlimitedIconData.add(UnlimitedIcon(it.imageUrl, "", "", it.id.toInt()))
                            }
                            val limitedIconRVAdapter=ShopBannerNewLimitedIconRVAdapter(limitedIconData)
                            binding.myProfileCardIconLimitedRv.adapter=limitedIconRVAdapter
                            binding.myProfileCardIconLimitedRv.layoutManager=LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

                            val unlimitedIconRVAdapter=ShopBannerNewUnlimitedIconRVAdapter(unlimitedIconData)
                            binding.myProfileCardIconUnlimitedRv.adapter=unlimitedIconRVAdapter
                            binding.myProfileCardIconUnlimitedRv.layoutManager=LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        }
                        else if(type == "title") { // 칭호
                            limitedFameData = ArrayList()
                            unlimitedFameData = ArrayList()
                            responseData.limited.forEach {
                                limitedFameData.add(LimitedFame(it.imageUrl, "", it.id.toInt()))
                            }
                            responseData.always.forEach {
                                unlimitedFameData.add(UnlimitedFame(it.imageUrl, "", it.id.toInt()))
                            }

                            val limitedFameRVAdapter=ShopBannerNewLimitedFameRVAdapter(limitedFameData)
                            binding.myProfileCardFameLimitedRv.adapter=limitedFameRVAdapter
                            binding.myProfileCardFameLimitedRv.layoutManager=LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

                            val unlimitedFameRVAdapter=ShopBannerNewUnlimitedFameRVAdapter(unlimitedFameData)
                            binding.myProfileCardFameUnlimitedRv.adapter=unlimitedFameRVAdapter
                            binding.myProfileCardFameUnlimitedRv.layoutManager=LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        }
                    }
                }
                else {
                    Log.e("getMyitems()", "notSuccessful: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<MyItems>, t: Throwable) {
                Log.e("getMyitems()", "onFailure: $t")
            }

        })
    }

    fun getMyProfile(context: FragmentActivity) {
        //내 프로필 정보 불러오기
        RetrofitAPI.myService.getMyProfile("Bearer $userToken").enqueue(object:
            Callback<MyProfile> {
            override fun onResponse(call: Call<MyProfile>, response: Response<MyProfile>) {
                if(response.isSuccessful) {
                    val responseData = response.body()
                    if(responseData != null) {
                        Log.d("getMyProfile()", "Response: ${responseData}")
                        binding.myProfileCardNameTv.text = responseData.nickname
                        binding.myProfileCardPointTv.text = responseData.point.toString()
                        if(responseData.profileImg != null) bindImg(context, responseData.profileImg, binding.myProfileCardCiv)
                        if(responseData.userIconImg != null) bindSvgImg(context, responseData.userIconImg, binding.myIconIv)
                        if(responseData.userTitleImg != null) bindSvgImg(context, responseData.userTitleImg, binding.myProfileCardBadgeIv)
                    }
                }
                else {
                    Log.e("getMyProfile()", "notSuccessful: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<MyProfile>, t: Throwable) {
                Log.e("getMyProfile()", "onFailure: $t")
            }

        })
    }

    //Glide로 이미지 등록
    fun bindImg(context: FragmentActivity, img: String, target: ImageView) {
        Glide.with(context)
            .load(img)
            .placeholder(null)
            .into(target)
    }


    //Glide로 svg이미지 불러오기
    fun bindSvgImg(context: FragmentActivity, img: String, target: ImageView) {
        //svg이미지 로더
        val imageLoader = ImageLoader.Builder(context)
            .componentRegistry {
                add(SvgDecoder(context)) // SVG 이미지 처리를 위해 SvgDecoder 추가
            }
            .build()
        val imageRequest = ImageRequest.Builder(context)
            .data(img)
            .target(target)  // 해당 이미지뷰를 타겟으로 svg 삽입
            .build()
        imageLoader.enqueue(imageRequest)
    }

    fun checkLoginStatus(context: FragmentActivity) {
        // SharedPreferences에서 액세스 토큰 가져오기
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        userToken = sharedPreferences.getString(LoginActivity.ACCESS_TOKEN_KEY, "") ?: ""

        if (userToken.isNotEmpty()) {
            Log.d("MyFragment", ">> 로그인 상태입니다.")
        }else{
            // 비회원 상태인 경우
            Log.d("MyFragment", ">> 비회원 상태입니다.")
        }
    }

}