package com.example.favoriteplace

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.favoriteplace.databinding.ItemShopBannerNewFameBinding

class ShopBannerNewUnlimitedFameRVAdapter (private val unlimitedFameList: ArrayList<UnlimitedFame>):RecyclerView.Adapter<ShopBannerNewUnlimitedFameRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ShopBannerNewUnlimitedFameRVAdapter.ViewHolder {
        val binding: ItemShopBannerNewFameBinding= ItemShopBannerNewFameBinding.inflate(
            LayoutInflater.from(viewGroup.context),viewGroup,false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int=unlimitedFameList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(unlimitedFameList[position])
    }

    inner class ViewHolder(val binding: ItemShopBannerNewFameBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(unlimitedFame: UnlimitedFame){
            binding.itemShopBannerNewFameIv.setImageResource(unlimitedFame.fameImg!!)
            binding.itemShopBannerNewFameTv.text=unlimitedFame.cost
        }

    }
}