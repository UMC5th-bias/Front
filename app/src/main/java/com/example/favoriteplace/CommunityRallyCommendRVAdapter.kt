package com.example.favoriteplace

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.favoriteplace.databinding.ItemCommunityRallyCommendBinding

class CommunityRallyCommendRVAdapter (private val commendList: ArrayList<MyComments>,
                                      private val clickListener: CommunityRallyCommendRVAdapter.RallyCommendClickListener
):
    RecyclerView.Adapter<CommunityRallyCommendRVAdapter.ViewHolder>() {

    interface RallyCommendClickListener {
        fun onRallyCommendClicked(guestBookId: Long)
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemCommunityRallyCommendBinding = ItemCommunityRallyCommendBinding.inflate(
            LayoutInflater.from(viewGroup.context),viewGroup,false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int=commendList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(commendList[position])
    }

    inner class ViewHolder(val binding: ItemCommunityRallyCommendBinding): RecyclerView.ViewHolder(binding.root){
        init {
            // ViewHolder 객체가 생성될 때 실행
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val guestBookId = commendList[position].myGuestBookInfo.id
                    clickListener.onRallyCommendClicked(guestBookId)
                }
            }
        }
        fun bind(commend: MyComments){
            binding.itemCommunityRallyCommendDayTv.text=commend.passedTime
            binding.itemCommunityRallyCommendTv.text=commend.content
            binding.itemCommunityRallyCommendTitleTv.text=commend.myGuestBookInfo.title
            binding.itemCommunityRallyCommendWriterTv.text=commend.myGuestBookInfo.nickname
            binding.itemCommunityRallyCommendEyeTv.text= commend.myGuestBookInfo.views.toString()
            binding.itemCommunityRallyCommendLikeTv.text= commend.myGuestBookInfo.likes.toString()
            binding.itemCommunityRallyCommendClockTv.text= commend.myGuestBookInfo.passedTime
            binding.itemCommunityRallyCommendCommentNumTv.text= commend.myGuestBookInfo.comments.toString()

            // 설정한 TextView에 ... 줄임 처리 설정
            binding.itemCommunityRallyCommendTitleTv.ellipsize = TextUtils.TruncateAt.END
            binding.itemCommunityRallyCommendTitleTv.maxLines = 1 // 최대 표시할 줄 수

            // 설정한 TextView에 ... 줄임 처리 설정
            binding.itemCommunityRallyCommendTv.ellipsize = TextUtils.TruncateAt.END
            binding.itemCommunityRallyCommendTv.maxLines = 1 // 최대 표시할 줄 수
        }

    }
}