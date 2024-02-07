package com.example.favoriteplace

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.favoriteplace.databinding.ItemRallyplaceCitylistBinding

class RallyPlaceCityRVAdapter(
    private val context: Context,
    private val cityList: Map<String, List<RallyPlaceLocationItem>>
) : RecyclerView.Adapter<RallyPlaceCityRVAdapter.viewHolder>() {

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): RallyPlaceCityRVAdapter.viewHolder {
        val binding: ItemRallyplaceCitylistBinding = ItemRallyplaceCitylistBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup,false)
        return viewHolder(binding)
    }

    override fun getItemCount(): Int {
        return cityList.size
    }

    override fun onBindViewHolder(holder: RallyPlaceCityRVAdapter.viewHolder, position: Int) {
        holder.bind(cityList.keys.toList()[position], cityList.values.toList()[position])
    }

    inner class viewHolder(val binding: ItemRallyplaceCitylistBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(city: String, locationInfoList: List<RallyPlaceLocationItem>){
            binding.rallyplaceCityName.text = city
            binding.rallyplaceCityName.setOnClickListener {
                if(binding.rallyplaceLocationRV.isVisible) binding.rallyplaceLocationRV.visibility = View.GONE
                else binding.rallyplaceLocationRV.visibility = View.VISIBLE
            }

            val rallyPlaceLocationRVAdapter = RallyPlaceLocationRVAdapter(locationInfoList)
            binding.rallyplaceLocationRV.adapter = rallyPlaceLocationRVAdapter
            binding.rallyplaceLocationRV.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        }
    }

}