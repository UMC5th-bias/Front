package com.example.favoriteplace

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.favoriteplace.databinding.ItemTrendingPostBinding

class TrendingPostsAdapter(private val trendingPost : List<HomeService.TrendingPosts>) : RecyclerView.Adapter<TrendingPostsAdapter.TrendingPostViewHolder>() {

    private var trendingPosts: List<HomeService.TrendingPosts> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingPostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTrendingPostBinding.inflate(inflater, parent, false)

        Log.d("HomeFragment", ">> TrendingPostsAdapter success ")
        return TrendingPostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrendingPostViewHolder, position: Int) {
        val trendingPost = trendingPosts[position]
        holder.bind(trendingPost)
    }

    override fun getItemCount(): Int = trendingPosts.size


    fun submitList(newList: List<HomeService.TrendingPosts>) {
        trendingPosts = newList
        notifyDataSetChanged() // 데이터 변경사항을 알려줌
    }

    inner class TrendingPostViewHolder(val binding: ItemTrendingPostBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // View binding 프로퍼티들 초기화
        private val title1 = binding.homeItemTitleTv1
        private val rank = binding.homeItemRank
        private val profile = binding.homeItemCiv1
        private val icon = binding.homeMemberIconIv1
        private val time1 = binding.homeItemTimeTv
        private val board1 = binding.homeItemCategoryTv1
        private val tag1 = binding.homeItemTag1Tv1
        private val tag2=  binding.homeItemTag2Tv1


        // 데이터 바인딩 메서드
        fun bind(post: HomeService.TrendingPosts) {
            binding.apply {
                title1.text = post.title
                time1.text = post.passedTime
                board1.text = post.board
                rank.text=post.rank.toString()

                tag1.text = post.hashtags?.getOrNull(0)
                tag2.text =  post.hashtags?.getOrNull(1)

                // Glide 또는 다른 라이브러리를 사용하여 이미지 로드
                Glide.with(itemView)
                    .load(post.profileImageUrl)
                    .placeholder(R.drawable.signup_default_profile_image) // 이미지 로딩 중에 표시할 placeholder 이미지
                    .into(profile)

                // 사용자 아이콘
                post.profileIconUrl?.let { url ->
                    bind(binding.root.context, url, icon)
                }

            }
        }
    }
}
