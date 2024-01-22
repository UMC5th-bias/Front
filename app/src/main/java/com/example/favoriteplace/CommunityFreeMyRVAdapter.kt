package com.example.favoriteplace

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.favoriteplace.databinding.ItemCommunityMyBinding
import java.util.ArrayList

class CommunityFreeMyRVAdapter(private val freeMyWriteList: ArrayList<FreeMyWrite>): RecyclerView.Adapter<CommunityFreeMyRVAdapter.ViewHolder>(){

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemCommunityMyBinding = ItemCommunityMyBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int=freeMyWriteList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(freeMyWriteList[position])
    }
    inner class ViewHolder(val binding: ItemCommunityMyBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(freeRecommend: FreeMyWrite){
            binding.itemCommunityFreeMyTitleTv.text=freeRecommend.title
            binding.itemCommunityFreeMyWriterTv.text=freeRecommend.writer
            binding.itemCommunityFreeMyEyeTv.text= freeRecommend.eye.toString()
            binding.itemCommunityFreeMyLikeTv.text=freeRecommend.like.toString()
            binding.itemCommunityFreeMyClockTv.text= freeRecommend.time
            binding.itemCommunityFreeMyCommentNumTv.text= freeRecommend.commentNum.toString()
        }

    }
}