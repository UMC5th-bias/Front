package com.example.favoriteplace

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.favoriteplace.databinding.ItemCommunityRallyLatelyRecommendBinding
import kotlin.collections.ArrayList

class CommunityRallyLatelyRVAdapter(private val rallyLatelyList: ArrayList<GuestBook>): RecyclerView.Adapter<CommunityRallyLatelyRVAdapter.ViewHolder>(){
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): CommunityRallyLatelyRVAdapter.ViewHolder {
        val binding: ItemCommunityRallyLatelyRecommendBinding = ItemCommunityRallyLatelyRecommendBinding.inflate(
            LayoutInflater.from(viewGroup.context),viewGroup, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int=rallyLatelyList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(rallyLatelyList[position])
    }

    inner class ViewHolder(val binding: ItemCommunityRallyLatelyRecommendBinding): RecyclerView.ViewHolder(binding.root){

        //ViewHolder 객체가 생성될 때 실행
        init {
            binding.root.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {

                //뷰가 윈도우에 연결될 때 호출
                override fun onViewAttachedToWindow(v: View) {
                    loadImage() //이미지를 로드하는 함수
                }

                override fun onViewDetachedFromWindow(v: View) {

                }
            })

            // 설정한 TextView에 ... 줄임 처리 설정
            binding.itemCommunityRallyTitleTv.ellipsize = TextUtils.TruncateAt.END
            binding.itemCommunityRallyTitleTv.maxLines = 1 // 최대 표시할 줄 수
        }

        fun bind(rallyLately: GuestBook){
            binding.itemCommunityRallyTitleTv.text=rallyLately.title
            binding.itemCommunityRallyWriterTv.text=rallyLately.nickname
            binding.itemCommunityRallyEyeTv.text= rallyLately.views.toString()
            binding.itemCommunityRallyLikeTv.text=rallyLately.likes.toString()
            binding.itemCommunityRallyClockTv.text= rallyLately.passedTime
            binding.itemCommunityRallyCommentTv.text= rallyLately.comments.toString()
            if(rallyLately.hashTags.isNotEmpty()){
                binding.itemCommunityRallyTag1Tv.text= rallyLately.hashTags[0].toString()
                if(rallyLately.hashTags.size>=2){
                    binding.itemCommunityRallyTag2Tv.text=rallyLately.hashTags[1].toString()
                } else {
                    binding.itemCommunityRallyTag2TextTv.visibility=View.INVISIBLE
                }
            } else {
                binding.itemCommunityRallyTag1TextTv.visibility=View.INVISIBLE
                binding.itemCommunityRallyTag2TextTv.visibility=View.INVISIBLE
            }
        }

        private fun loadImage() {
            val rallyLately = rallyLatelyList[adapterPosition]
            val imageView = binding.itemCommunityRallyIv
            val url = rallyLately.thumbnail

            Glide.with(itemView.context)
                .load(url)
                .into(imageView)
        }
    }

}