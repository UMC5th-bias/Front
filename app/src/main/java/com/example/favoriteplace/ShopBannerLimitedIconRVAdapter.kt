package com.example.favoriteplace

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.favoriteplace.databinding.ItemShopBannerNewIconBinding

class ShopBannerLimitedIconRVAdapter(private val limitedIconList: List<LimitedIcon>):RecyclerView.Adapter<ShopBannerLimitedIconRVAdapter.ViewHolder>() {

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
        holder.bind(limitedIconList[position])
        holder.itemView.setOnClickListener{
            mItemClickListener.onItemClick()
        }
    }
    inner class ViewHolder(val binding: ItemShopBannerNewIconBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(limitedIcon: LimitedIcon){
            binding.itemShopBannerNewIconIv.setImageResource(limitedIcon.iconImg!!)
            binding.itemShopBannerNewIconTitleTv.text=limitedIcon.title
            binding.itemShopBannerNewIconCostTv.text=limitedIcon.cost
        }

    }
}