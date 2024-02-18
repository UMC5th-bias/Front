package com.example.favoriteplace

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.favoriteplace.databinding.ItemCommunityRallyLatelyRecommendBinding
import kotlin.collections.ArrayList

class CommunityRallyLatelyRVAdapter(private val rallyLatelyList: ArrayList<GuestBook>
): RecyclerView.Adapter<CommunityRallyLatelyRVAdapter.ViewHolder>(){

    // 클릭 이벤트를 처리할 리스너 인터페이스 정의
    interface OnItemClickListener {
        fun onItemClick(postId: Int)
    }

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

            //만일 해시태그가 있다면
            if(rallyLately.hashTags.isNotEmpty()){
                binding.itemCommunityRallyTag1Tv.text= rallyLately.hashTags[0]
                if(rallyLately.hashTags.size>=2){   //만일 해시태그가 두 개라면
                    binding.itemCommunityRallyTag2Tv.text=rallyLately.hashTags[1]
                } else {    //만일 해시태그가 하나라면
                    binding.itemCommunityRallyTag2TextTv.visibility=View.INVISIBLE
                }
            } else {    //만일 해시태그가 없다면
                binding.itemCommunityRallyTag1TextTv.visibility=View.INVISIBLE
                binding.itemCommunityRallyTag2TextTv.visibility=View.INVISIBLE
            }
        }

        //이미지를 로드하는 함수
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