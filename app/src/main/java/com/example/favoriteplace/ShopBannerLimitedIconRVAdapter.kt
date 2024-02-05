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
import com.squareup.picasso.Picasso
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.load
import coil.request.ImageRequest

class ShopBannerLimitedIconRVAdapter(private val limitedIconList: ArrayList<ShopMainLimitedIcon>):RecyclerView.Adapter<ShopBannerLimitedIconRVAdapter.ViewHolder>() {

    //RVA에서 setOnClickListener을 쓸 수 있도록 하는 인터페이스
    interface MyItemClickListener : ShopBannerNewLimitedFameRVAdapter.MyItemClickListener {
        override fun onItemClick()
    }

    //전달받은 리스너 객체를 저장하는 변수
    private lateinit var mItemClickListener: ShopBannerNewLimitedFameRVAdapter.MyItemClickListener
    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener=itemClickListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ShopBannerLimitedIconRVAdapter.ViewHolder {
        val binding: ItemShopBannerNewIconBinding=ItemShopBannerNewIconBinding.inflate(
            LayoutInflater.from(viewGroup.context),viewGroup,false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int=3

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position >= 0 && position < limitedIconList.size) {
            holder.bind(limitedIconList[position])
            holder.itemView.setOnClickListener {
                mItemClickListener.onItemClick()
            }
        } else {
            // 리스트가 비어있거나 유효한 위치가 아닌 경우에 대한 처리
            // 여기에서 에러 메시지 출력 또는 다른 대체 동작을 수행할 수 있습니다.
            Log.e("ShopBannerLimitedIconRVAdapter", "Invalid position: $position")
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

                // 이미지 로딩에 실패한 경우 기본 이미지를 설정하거나 다른 조치를 취할 수 있습니다.
                // 예를 들어, 다른 이미지로 대체하거나 오류 메시지를 표시할 수 있습니다.
                // binding.itemShopBannerNewIconIv.setImageResource(R.drawable.default_image)
            }
        }

    }
}