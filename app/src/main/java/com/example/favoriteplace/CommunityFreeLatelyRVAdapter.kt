package com.example.favoriteplace

import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.favoriteplace.databinding.ItemCommunityFreeLatelyBinding
import java.util.ArrayList

class CommunityFreeLatelyRVAdapter (private val latelyList: ArrayList<Posts>,
                                    private val listener: OnItemClickListener
): RecyclerView.Adapter<CommunityFreeLatelyRVAdapter.ViewHolder>() {

    // 클릭 이벤트를 처리할 리스너 인터페이스 정의
    interface OnItemClickListener {
        fun onItemClick(postId: Int)
    }

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
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(latelyList[position].id)
                }
            }
        }

        fun bind(latelywrite: Posts){
            binding.itemCommunityFreeLatelyTitleTv.text=latelywrite.title
            binding.itemCommunityFreeLatelyWriterTv.text=latelywrite.nickname
            binding.itemCommunityFreeLatelyEyeTv.text= latelywrite.views.toString()
            binding.itemCommunityFreeLatelyLikeTv.text= latelywrite.likes.toString()
            binding.itemCommunityFreeLatelyClockTv.text= latelywrite.passedTime
            binding.itemCommunityFreeLatelyCommentNumTv.text= latelywrite.comments.toString()

            // 설정한 TextView에 ... 줄임 처리 설정
            binding.itemCommunityFreeLatelyTitleTv.ellipsize = TextUtils.TruncateAt.END
            binding.itemCommunityFreeLatelyTitleTv.maxLines = 1 // 최대 표시할 줄 수
        }

    }
}