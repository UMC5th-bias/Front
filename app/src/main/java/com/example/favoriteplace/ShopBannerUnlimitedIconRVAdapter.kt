package com.example.favoriteplace

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.favoriteplace.databinding.ItemShopBannerNewIconBinding

class ShopBannerUnlimitedIconRVAdapter(private val unlimitedIconList: ArrayList<ShopMainUnlimitedIcon>):
    RecyclerView.Adapter<ShopBannerUnlimitedIconRVAdapter.ViewHolder>(){

    //RVA에서 setOnClickListener을 쓸 수 있도록 하는 인터페이스
    interface MyItemClickListener : ShopBannerLimitedIconRVAdapter.MyItemClickListener {
        override fun onItemClick()
    }

    //전달받은 리스너 객체를 저장하는 변수
    private lateinit var mItemClickListener: ShopBannerNewLimitedFameRVAdapter.MyItemClickListener
    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener=itemClickListener
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ShopBannerUnlimitedIconRVAdapter.ViewHolder {
        val binding: ItemShopBannerNewIconBinding= ItemShopBannerNewIconBinding.inflate(
            LayoutInflater.from(viewGroup.context),viewGroup,false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int=unlimitedIconList.size

    override fun onBindViewHolder(
        holder: ShopBannerUnlimitedIconRVAdapter.ViewHolder,
        position: Int
    ) {
        holder.bind(unlimitedIconList[position])
        holder.itemView.setOnClickListener{
            mItemClickListener.onItemClick()
        }
    }

    inner class ViewHolder(val binding: ItemShopBannerNewIconBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(unlimitedIcon: ShopMainUnlimitedIcon) {
            try {
                val imageLoader = ImageLoader.Builder(binding.root.context)
                    .componentRegistry {
                        add(SvgDecoder(binding.root.context)) // SVG 이미지 처리를 위해 SvgDecoder 추가
                    }
                    .build()

                val imageRequest = ImageRequest.Builder(binding.root.context)
                    .crossfade(true)
                    .crossfade(300)
                    .data(unlimitedIcon.iconImg)
                    .target(binding.itemShopBannerNewIconIv)
                    .build()
                imageLoader.enqueue(imageRequest)

                // 이미지 로딩이 성공한 경우에만 아래 코드 실행
                binding.itemShopBannerNewIconTitleTv.text = unlimitedIcon.title
                binding.itemShopBannerNewIconCostTv.text = unlimitedIcon.cost

            } catch (e: Exception) {
                // Glide에서 발생하는 예외 처리
                Log.e("ViewHolder", "Error loading image: ${e.message}")
                e.printStackTrace()

            }
        }
    }
}