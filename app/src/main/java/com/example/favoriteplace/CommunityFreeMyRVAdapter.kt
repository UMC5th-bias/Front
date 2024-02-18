package com.example.favoriteplace

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.favoriteplace.databinding.ItemCommunityFreeMyBinding
import kotlin.collections.ArrayList

class CommunityFreeMyRVAdapter(private val freeMyWriteList: ArrayList<Posts>,
                               private val listener: OnItemClickListener
): RecyclerView.Adapter<CommunityFreeMyRVAdapter.ViewHolder>(){

    // 클릭 이벤트를 처리할 리스너 인터페이스 정의
    interface OnItemClickListener {
        fun onItemClick(postId: Int)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemCommunityFreeMyBinding = ItemCommunityFreeMyBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int=freeMyWriteList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(freeMyWriteList[position])
    }
    inner class ViewHolder(val binding: ItemCommunityFreeMyBinding): RecyclerView.ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(freeMyWriteList[position].id)
                }
            }
        }

        fun bind(freeRecommend: Posts){
            binding.itemCommunityFreeMyTitleTv.text=freeRecommend.title
            binding.itemCommunityFreeMyWriterTv.text=freeRecommend.nickname
            binding.itemCommunityFreeMyEyeTv.text= freeRecommend.views.toString()
            binding.itemCommunityFreeMyLikeTv.text=freeRecommend.likes.toString()
            binding.itemCommunityFreeMyClockTv.text= freeRecommend.passedTime
            binding.itemCommunityFreeMyCommentNumTv.text= freeRecommend.comments.toString()
        }

    }
}