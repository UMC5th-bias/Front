package com.example.favoriteplace

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.favoriteplace.databinding.ItemRallyHeartVerifyBinding
import java.util.ArrayList

class MyCompleteRallyRVAdapter (private val myCompleteRally: ArrayList<MyCompleteRally>): RecyclerView.Adapter<MyCompleteRallyRVAdapter.ViewHolder>(){
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): MyCompleteRallyRVAdapter.ViewHolder {
        val binding: ItemRallyHeartVerifyBinding = ItemRallyHeartVerifyBinding.inflate(
            LayoutInflater.from(viewGroup.context),viewGroup, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int=myCompleteRally.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(myCompleteRally[position])
    }

    inner class ViewHolder(val binding: ItemRallyHeartVerifyBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(myCompleteRally: MyCompleteRally){
            binding.itemRallyHeartVerifyTitleTv.text=myCompleteRally.title
            binding.itemRallyHeartVerifyRallyNowTv.text= myCompleteRally.progress.toString()
            binding.itemRallyHeartVerifyRallyTotalTv.text= myCompleteRally.total.toString()
            binding.itemRallyHeartVerifyRallyIv.setImageResource(myCompleteRally.coverImg!!)
        }

    }

}