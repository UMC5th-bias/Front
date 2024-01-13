package com.example.favoriteplace

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.favoriteplace.databinding.ItemCommunityFreeLatelyBinding
import java.util.ArrayList

class CommunityFreeLatelyRVAdapter (private val latelyList: ArrayList<FreeLatelyWrite>): RecyclerView.Adapter<CommunityFreeLatelyRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): CommunityFreeLatelyRVAdapter.ViewHolder {
        val binding: ItemCommunityFreeLatelyBinding = ItemCommunityFreeLatelyBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(latelyList[position])
    }

    override fun getItemCount(): Int=latelyList.size

    inner class ViewHolder(val binding: ItemCommunityFreeLatelyBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(latelywrite: FreeLatelyWrite){
            binding.itemCommunityFreeLatelyTitleTv.text=latelywrite.title
            binding.itemCommunityFreeLatelyWriterTv.text=latelywrite.writer
            binding.itemCommunityFreeLatelyEyeTv.text= latelywrite.eye.toString()
            binding.itemCommunityFreeLatelyLikeTv.text= latelywrite.like.toString()
            binding.itemCommunityFreeLatelyClockTv.text= latelywrite.clock
            binding.itemCommunityFreeLatelyCommentNumTv.text= latelywrite.comment_num.toString()
        }

    }
}