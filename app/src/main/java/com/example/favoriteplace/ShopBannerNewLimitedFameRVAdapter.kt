package com.example.favoriteplace

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.favoriteplace.databinding.ItemShopBannerNewFameBinding

class ShopBannerNewLimitedFameRVAdapter (private val limitedFameList: ArrayList<LimitedFame>):RecyclerView.Adapter<ShopBannerNewLimitedFameRVAdapter.ViewHolder>(){

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
    ): ShopBannerNewLimitedFameRVAdapter.ViewHolder {
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
        fun bind(limitedFame: LimitedFame){
            binding.itemShopBannerNewFameIv.setImageResource(limitedFame.fameImg!!)
            binding.itemShopBannerNewFameTv.text=limitedFame.cost
        }

    }
}