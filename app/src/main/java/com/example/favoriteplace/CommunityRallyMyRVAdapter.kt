package com.example.favoriteplace

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.favoriteplace.databinding.ItemCommunityRallyMyBinding
import java.util.ArrayList

class CommunityRallyMyRVAdapter(private val rallyMyWriteList: ArrayList<RallyMyWrite>): RecyclerView.Adapter<CommunityRallyMyRVAdapter.ViewHolder>(){

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemCommunityRallyMyBinding = ItemCommunityRallyMyBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int=rallyMyWriteList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(rallyMyWriteList[position])
    }
    inner class ViewHolder(val binding: ItemCommunityRallyMyBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(freeRecommend: RallyMyWrite){
            binding.itemCommunityRallyMyTitleTv.text=freeRecommend.title
            binding.itemCommunityRallyMyWriterTv.text=freeRecommend.writer
            binding.itemCommunityRallyMyEyeTv.text= freeRecommend.eye.toString()
            binding.itemCommunityRallyMyLikeTv.text=freeRecommend.like.toString()
            binding.itemCommunityRallyMyClockTv.text= freeRecommend.time
            binding.itemCommunityRallyMyCommentNumTv.text= freeRecommend.commentNum.toString()
        }

    }
}