package com.example.favoriteplace

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import com.example.favoriteplace.databinding.ItemShopBannerNewFameBinding
import com.google.gson.Gson

class ShopBannerNewLimitedFameRVAdapter(private val limitedFameList: ArrayList<LimitedFame>):RecyclerView.Adapter<ShopBannerNewLimitedFameRVAdapter.ViewHolder>(){

    //RVA에서 setOnClickListener을 쓸 수 있도록 하는 인터페이스
    interface MyItemClickListener{
        fun onItemLimitedFameClick(limitedFame: LimitedFame)
    }

    //전달받은 리스너 객체를 저장하는 변수
    private lateinit var mItemClickListener: MyItemClickListener
    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener=itemClickListener
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ShopBannerNewLimitedFameRVAdapter.ViewHolder {
        val binding: ItemShopBannerNewFameBinding=ItemShopBannerNewFameBinding.inflate(
            LayoutInflater.from(viewGroup.context),viewGroup,false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int=limitedFameList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position >= 0 && position < limitedFameList.size){
            holder.bind(limitedFameList[position])
            holder.itemView.setOnClickListener{
                mItemClickListener.onItemLimitedFameClick(limitedFameList[position])
            }
        }

    }

    inner class ViewHolder(val binding: ItemShopBannerNewFameBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(limitedFame: LimitedFame){
            try {
                val imageLoader = ImageLoader.Builder(binding.root.context)
                    .componentRegistry {
                        add(SvgDecoder(binding.root.context)) // SVG 이미지 처리를 위해 SvgDecoder 추가
                    }
                    .build()

                val imageRequest = ImageRequest.Builder(binding.root.context)
                    .crossfade(true)
                    .crossfade(300)  //애니메이션 처리
                    .data(limitedFame.fameImg)
                    .target(binding.itemShopBannerNewFameIv)  //해당 이미지뷰를 타겟으로 svg 삽입
                    .build()
                imageLoader.enqueue(imageRequest)

                binding.itemShopBannerNewFameTv.text = limitedFame.cost
                if(limitedFame.cost == "사용중" || limitedFame.cost == "") {  // 마이페이지 프로필 카드 편집용
                    binding.itemShopBannerPTv.text = ""
                }

            } catch (e: Exception) {
                Log.e("ViewHolder", "Error loading image: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}