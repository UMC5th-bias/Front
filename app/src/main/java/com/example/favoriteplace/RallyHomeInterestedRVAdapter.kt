package com.example.favoriteplace

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.favoriteplace.databinding.ItemRallyhomeInterestedrallyBinding
import kotlin.math.min

class RallyHomeInterestedRVAdapter(private val items: List<InterestedRallyItem>) : RecyclerView.Adapter<RallyHomeInterestedRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRallyhomeInterestedrallyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int=items.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(private val binding: ItemRallyhomeInterestedrallyBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: InterestedRallyItem) {
            binding.interestedAnimationTitleTv.text = item.title
            binding.interestedAnimationIv.setImageResource(item.imageResId)
            // 여기에 데이터 바인딩 로직 구현
            // 예: binding.textView.text = item.title
        }
    }
}
