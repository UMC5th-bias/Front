package com.example.favoriteplace

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.favoriteplace.databinding.ItemShopBannerNewIconBinding

class ShopBannerUnlimitedIconRVAdapter(private val unlimitedIconList: List<UnlimitedIcon>):
    RecyclerView.Adapter<ShopBannerUnlimitedIconRVAdapter.ViewHolder>(){

    //RVA에서 setOnClickListener을 쓸 수 있도록 하는 인터페이스
    interface MyItemClickListener : ShopBannerNewLimitedFameRVAdapter.MyItemClickListener {
        override fun onItemClick()
    }

    //전달받은 리스너 객체를 저장하는 변수
    private lateinit var mItemClickListener: ShopBannerNewLimitedFameRVAdapter.MyItemClickListener
    fun setMyItemClickListener(itemClickListener: ShopBannerNewLimitedIconRVAdapter.MyItemClickListener){
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
        fun bind(unlimitedIcon: UnlimitedIcon){
            binding.itemShopBannerNewIconIv.setImageResource(unlimitedIcon.iconImg!!)
            binding.itemShopBannerNewIconTitleTv.text=unlimitedIcon.title
            binding.itemShopBannerNewIconCostTv.text=unlimitedIcon.cost
        }
    }
}