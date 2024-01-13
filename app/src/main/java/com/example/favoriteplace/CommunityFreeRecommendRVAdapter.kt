package com.example.favoriteplace

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.favoriteplace.databinding.ItemCommunityFreeRecommendBinding
import java.util.ArrayList

class CommunityFreeRecommendRVAdapter (private val freeRecommendList: ArrayList<FreeRecommend>): RecyclerView.Adapter<CommunityFreeRecommendRVAdapter.ViewHolder>(){
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): CommunityFreeRecommendRVAdapter.ViewHolder {
        val binding: ItemCommunityFreeRecommendBinding = ItemCommunityFreeRecommendBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int=freeRecommendList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(freeRecommendList[position])
    }

    inner class ViewHolder(val binding: ItemCommunityFreeRecommendBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(freeRecommend: FreeRecommend){
            binding.itemCommunityFreeRecommendTitleTv.text=freeRecommend.title
            binding.itemCommunityFreeRecommendWriterTv.text=freeRecommend.writer
            binding.itemCommunityFreeRecommendEyeTv.text= freeRecommend.eye.toString()
            binding.itemCommunityFreeRecommendLikeTv.text=freeRecommend.like.toString()
            binding.itemCommunityFreeRecommendClockTv.text= freeRecommend.clock
            binding.itemCommunityFreeRecommendCommentNumTv.text= freeRecommend.comment_num.toString()
        }

    }

}