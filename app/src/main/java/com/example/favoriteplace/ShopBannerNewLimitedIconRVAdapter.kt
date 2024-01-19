package com.example.favoriteplace

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.favoriteplace.databinding.ItemShopBannerNewIconBinding

class ShopBannerNewLimitedIconRVAdapter (private val limitedIconList: ArrayList<LimitedIcon>):RecyclerView.Adapter<ShopBannerNewLimitedIconRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ShopBannerNewLimitedIconRVAdapter.ViewHolder {
        val binding: ItemShopBannerNewIconBinding=ItemShopBannerNewIconBinding.inflate(
            LayoutInflater.from(viewGroup.context),viewGroup,false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int=limitedIconList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(limitedIconList[position])
    }
    inner class ViewHolder(val binding: ItemShopBannerNewIconBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(limitedIcon: LimitedIcon){
            binding.itemShopBannerNewIconIv.setImageResource(limitedIcon.iconImg!!)
            binding.itemShopBannerNewIconTitleTv.text=limitedIcon.title
            binding.itemShopBannerNewIconCostTv.text=limitedIcon.cost
        }

    }
}