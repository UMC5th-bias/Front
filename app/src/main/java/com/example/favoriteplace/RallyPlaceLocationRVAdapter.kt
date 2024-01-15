package com.example.favoriteplace

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.favoriteplace.databinding.ItemRallyplaceLocationlistBinding

class RallyPlaceLocationRVAdapter(private val locationInfoList: List<RallyPlaceLocationItem>) : RecyclerView.Adapter<RallyPlaceLocationRVAdapter.viewHolder>() {
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): RallyPlaceLocationRVAdapter.viewHolder {
        val binding: ItemRallyplaceLocationlistBinding = ItemRallyplaceLocationlistBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup,false)
        return viewHolder(binding)
    }

    override fun getItemCount(): Int {
        return locationInfoList.size
    }

    override fun onBindViewHolder(holder: RallyPlaceLocationRVAdapter.viewHolder, position: Int) {
        holder.bind(locationInfoList[position])
    }

    inner class viewHolder(val binding: ItemRallyplaceLocationlistBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(info: RallyPlaceLocationItem){
            binding.rallyplaceLocationAni.text = info.name
            binding.rallyplaceLocationLoc.text = info.location
            binding.rallyplaceLocationImg.setImageResource(info.src)
        }
    }

}