package com.example.favoriteplace

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.favoriteplace.databinding.ItemShopBannerNewIconBinding

class ShopBannerNewUnlimitedIconRVAdapter (private val unlimitedIconList: ArrayList<UnlimitedIcon>):
    RecyclerView.Adapter<ShopBannerNewUnlimitedIconRVAdapter.ViewHolder>(){
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ShopBannerNewUnlimitedIconRVAdapter.ViewHolder {
        val binding: ItemShopBannerNewIconBinding= ItemShopBannerNewIconBinding.inflate(
            LayoutInflater.from(viewGroup.context),viewGroup,false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int=unlimitedIconList.size

    override fun onBindViewHolder(
        holder: ShopBannerNewUnlimitedIconRVAdapter.ViewHolder,
        position: Int
    ) {
        holder.bind(unlimitedIconList[position])
    }

    inner class ViewHolder(val binding: ItemShopBannerNewIconBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(unlimitedIcon: UnlimitedIcon){
            binding.itemShopBannerNewIconIv.setImageResource(unlimitedIcon.iconImg!!)
            binding.itemShopBannerNewIconTitleTv.text=unlimitedIcon.title
            binding.itemShopBannerNewIconCostTv.text=unlimitedIcon.cost
        }
    }
}