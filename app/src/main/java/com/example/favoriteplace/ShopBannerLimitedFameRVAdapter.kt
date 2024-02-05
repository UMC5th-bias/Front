package com.example.favoriteplace

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.example.favoriteplace.databinding.ItemShopBannerNewFameBinding
import com.squareup.picasso.Picasso
import java.net.URI
import javax.sql.DataSource

class ShopBannerLimitedFameRVAdapter(private val limitedFameList: ArrayList<ShopMainLimitedFame>):RecyclerView.Adapter<ShopBannerLimitedFameRVAdapter.ViewHolder>(){

    //RVA에서 setOnClickListener을 쓸 수 있도록 하는 인터페이스
    interface MyItemClickListener{
        fun onItemClick()
    }

    //전달받은 리스너 객체를 저장하는 변수
    private lateinit var mItemClickListener: MyItemClickListener
    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener=itemClickListener
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ShopBannerLimitedFameRVAdapter.ViewHolder {
        val binding: ItemShopBannerNewFameBinding=ItemShopBannerNewFameBinding.inflate(
            LayoutInflater.from(viewGroup.context),viewGroup,false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int=limitedFameList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(limitedFameList[position])
        holder.itemView.setOnClickListener{
            mItemClickListener.onItemClick()
        }
    }

    inner class ViewHolder(val binding: ItemShopBannerNewFameBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(limitedFame: ShopMainLimitedFame){
            try {
                val imageLoader = ImageLoader.Builder(binding.root.context)
                    .componentRegistry {
                        add(SvgDecoder(binding.root.context)) // SVG 이미지 처리를 위해 SvgDecoder 추가
                    }
                    .build()

                val imageRequest = ImageRequest.Builder(binding.root.context)
                    .crossfade(true)
                    .crossfade(300)
                    .data(limitedFame.fameImg)
                    .target(binding.itemShopBannerNewFameIv)
                    .build()
                imageLoader.enqueue(imageRequest)

                binding.itemShopBannerNewFameTv.text = limitedFame.cost
            } catch (e: Exception) {
                Log.e("ViewHolder", "Error loading image: ${e.message}")
                e.printStackTrace()
            }
        }

    }
}