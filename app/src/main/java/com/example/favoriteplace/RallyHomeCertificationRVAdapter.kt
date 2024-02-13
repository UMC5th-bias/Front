package com.example.favoriteplace

import android.content.Context
import com.example.favoriteplace.databinding.ItemRallyhomeCertificatedrallyBinding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlin.math.min

class RallyHomeCertificationRVAdapter(private val items: List<CertifiedRallyItem>, private val context: Context) : RecyclerView.Adapter<RallyHomeCertificationRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRallyhomeCertificatedrallyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        // 최대 2개의 항목만 표시
        return min(items.size, 2)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(private val binding: ItemRallyhomeCertificatedrallyBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CertifiedRallyItem) {
            binding.certificatedRallyTitleTv.text = item.title
            binding.certificatedRallyTag1Tv.text = item.tag1
            binding.certificatedRallyTag2Tv.text = item.tag2
            Glide.with(context as MainActivity)
                .load(item.imageResId)
                .into(binding.certificatedAnimationIv)
            binding.certificatedRallyTimeTv.text = item.time
            // 여기에 데이터 바인딩 로직 구현
            // 예: binding.textView.text = item.title
        }
    }
}
