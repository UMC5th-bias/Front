package com.example.favoriteplace

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.favoriteplace.databinding.ItemCommunityFreeLatelyBinding
import java.util.ArrayList

class CommunityFreeLatelyRVAdapter (private val latelyList: ArrayList<Posts>): RecyclerView.Adapter<CommunityFreeLatelyRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): CommunityFreeLatelyRVAdapter.ViewHolder {
        val binding: ItemCommunityFreeLatelyBinding = ItemCommunityFreeLatelyBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup, false)

        Log.d("CommunityFreeRVA","SUCCESS")
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(latelyList[position])
    }

    override fun getItemCount(): Int=latelyList.size

    inner class ViewHolder(val binding: ItemCommunityFreeLatelyBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(latelywrite: Posts){
            binding.itemCommunityFreeLatelyTitleTv.text=latelywrite.title
            binding.itemCommunityFreeLatelyWriterTv.text=latelywrite.nickname
            binding.itemCommunityFreeLatelyEyeTv.text= latelywrite.views.toString()
            binding.itemCommunityFreeLatelyLikeTv.text= latelywrite.likes.toString()
            binding.itemCommunityFreeLatelyClockTv.text= latelywrite.passedTime
            binding.itemCommunityFreeLatelyCommentNumTv.text= latelywrite.comments.toString()
        }

    }

//    fun addPosts(newPosts: List<CommunityPost>){
//        posts.addAll(newPosts)
//        notifyDataSetChanged()
//    }
}