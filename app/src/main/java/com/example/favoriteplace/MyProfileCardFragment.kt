package com.example.favoriteplace

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.favoriteplace.databinding.FragmentMyProfileCardBinding

class MyProfileCardFragment : Fragment() {
    lateinit var binding : FragmentMyProfileCardBinding
    private var limitedFameData=ArrayList<LimitedFame>()
    private var unlimitedFameData=ArrayList<UnlimitedFame>()
    private var limitedIconData=ArrayList<LimitedIcon>()
    private var unlimitedIconData=ArrayList<UnlimitedIcon>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyProfileCardBinding.inflate(inflater, container, false)
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
        //LimitedIcon, UnLimitedIcon 데이터 클래스의 자료형을 수정함에 따라 에러가 발생
//        limitedIconData.apply {
//            add(LimitedIcon(R.drawable.limited_icon_1,"유닝이","20000P"))
//            add(LimitedIcon(R.drawable.limited_icon_1,"Developer","10000P"))
//            add(LimitedIcon(R.drawable.limited_icon_1,"유닝이","20000P"))
//        }

//        unlimitedIconData.apply {
//            add(UnlimitedIcon(R.drawable.unlimited_icon_1,"별행성","10000P"))
//            add(UnlimitedIcon(R.drawable.unlimited_icon_2,"새턴","10000P"))
//            add(UnlimitedIcon(R.drawable.unlimited_icon_3,"초승달","10000P"))
//        }

        val limitedIconRVAdapter=ShopBannerNewLimitedIconRVAdapter(limitedIconData)
        binding.myProfileCardIconLimitedRv.adapter=limitedIconRVAdapter
        binding.myProfileCardIconLimitedRv.layoutManager=LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val unlimitedIconRVAdapter=ShopBannerNewUnlimitedIconRVAdapter(unlimitedIconData)
        binding.myProfileCardIconUnlimitedRv.adapter=unlimitedIconRVAdapter
        binding.myProfileCardIconUnlimitedRv.layoutManager=LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }


    private fun badge() {
        //LimitedFame, UnLimitedFame 데이터 클래스의 자료형을 수정함에 따라 에러가 발생
//        limitedFameData.apply{
//            add(LimitedFame(R.drawable.limited_fame_1,"5000P"))
//            add(LimitedFame(R.drawable.limited_fame_2,"30000P"))
//            add(LimitedFame(R.drawable.limited_fame_3,"300000P"))
//        }

//        unlimitedFameData.apply {
//            add(UnlimitedFame(R.drawable.unlimited_fame_1, "5000P"))
//            add(UnlimitedFame(R.drawable.unlimited_fame_2, "10000P"))
//            add(UnlimitedFame(R.drawable.unlimited_fame_3, "100000P"))
//        }
//        val limitedFameRVAdapter=ShopBannerNewLimitedFameRVAdapter(limitedFameData)
//        binding.myProfileCardFameLimitedRv.adapter=limitedFameRVAdapter
//        binding.myProfileCardFameLimitedRv.layoutManager=LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//
//        val unlimitedFameRVAdapter=ShopBannerNewUnlimitedFameRVAdapter(unlimitedFameData)
//        binding.myProfileCardFameUnlimitedRv.adapter=unlimitedFameRVAdapter
//        binding.myProfileCardFameUnlimitedRv.layoutManager=LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }


}