package com.example.favoriteplace


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.favoriteplace.databinding.ItemHomeRealTimeBinding


class HomeItemRVAdapter (private val homeItemList: ArrayList<HomeItem>) : RecyclerView.Adapter<HomeItemRVAdapter.viewHolder>(){


    // 뷰 홀더를 생성해줘야 할 때 호출되는 함수로, 아이템 뷰 객체를 만들어서 뷰홀더에 전달
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): HomeItemRVAdapter.viewHolder {
        val binding:ItemHomeRealTimeBinding = ItemHomeRealTimeBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup,false)
        return viewHolder(binding)
    }

    // 뷰홀더에 데이터를 바인딩해줘야 할 때마다 호출되는 함수로 자주 호출
    override fun onBindViewHolder(holder: HomeItemRVAdapter.viewHolder, position: Int) {
        holder.bind(homeItemList[position])
    }


    // 데이터 세트 크기를 알려주는 함수 => 리사이클러뷰가 마지막이 언제인지를 알게 된다.
    override fun getItemCount(): Int = homeItemList.size


    inner class viewHolder(val binding: ItemHomeRealTimeBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(homeItem: HomeItem){
            binding.itemImgIv.setImageResource(homeItem.coverImage!!)
            binding.itemTitleTv.text = homeItem.title
            binding.itemTag1Tv.text =homeItem.tag1
            binding.itemTag2Tv.text =homeItem.tag2
            binding.itemTimeTv.text=homeItem.time
            binding.itemMenuTv.text=homeItem.menu
        }
    }

}