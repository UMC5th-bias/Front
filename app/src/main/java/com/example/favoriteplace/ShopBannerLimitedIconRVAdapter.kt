package com.example.favoriteplace

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.favoriteplace.databinding.ItemShopBannerNewIconBinding
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.load
import coil.request.ImageRequest

class ShopBannerLimitedIconRVAdapter(private val limitedIconList: ArrayList<ShopMainLimitedIcon>):RecyclerView.Adapter<ShopBannerLimitedIconRVAdapter.ViewHolder>() {

    //RVA에서 setOnClickListener을 쓸 수 있도록 하는 인터페이스
    interface MyItemClickListener {
        fun onItemClick(itemId: Int)
    }

    //전달받은 리스너 객체를 저장하는 변수
    private lateinit var mItemClickListener: MyItemClickListener
    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener=itemClickListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ShopBannerLimitedIconRVAdapter.ViewHolder {
        val binding: ItemShopBannerNewIconBinding=ItemShopBannerNewIconBinding.inflate(
            LayoutInflater.from(viewGroup.context),viewGroup,false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int=limitedIconList.size

    override fun onBindViewHolder(holder: ShopBannerLimitedIconRVAdapter.ViewHolder, position: Int) {
        val limitedIcon = limitedIconList[position]
        holder.bind(limitedIcon)
        holder.itemView.setOnClickListener {
            mItemClickListener.onItemClick(limitedIcon.id)
        }
    }

    inner class ViewHolder(val binding: ItemShopBannerNewIconBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(limitedIcon: ShopMainLimitedIcon) {
            try {
                val imageLoader = ImageLoader.Builder(binding.root.context)
                    .componentRegistry {
                        add(SvgDecoder(binding.root.context)) // SVG 이미지 처리를 위해 SvgDecoder 추가
                    }
                    .build()

                val imageRequest = ImageRequest.Builder(binding.root.context)
                    .crossfade(true)
                    .crossfade(300)
                    .data(limitedIcon.iconImg)
                    .target(binding.itemShopBannerNewIconIv)
                    .build()
                imageLoader.enqueue(imageRequest)

                // 이미지 로딩이 성공한 경우에만 아래 코드 실행
                binding.itemShopBannerNewIconTitleTv.text = limitedIcon.title
                binding.itemShopBannerNewIconCostTv.text = limitedIcon.cost

            } catch (e: Exception) {
                // Glide에서 발생하는 예외 처리
                Log.e("ViewHolder", "Error loading image: ${e.message}")
                e.printStackTrace()

            }
        }

    }
}