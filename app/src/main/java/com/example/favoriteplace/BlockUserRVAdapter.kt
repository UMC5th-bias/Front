package com.example.favoriteplace

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.favoriteplace.databinding.ItemBlockUserBinding

class BlockUserRVAdapter (private val blockUserList: ArrayList<BlockUser>): RecyclerView.Adapter<BlockUserRVAdapter.ViewHolder>(){


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemBlockUserBinding=ItemBlockUserBinding.inflate(
            LayoutInflater.from(viewGroup.context),viewGroup,false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = blockUserList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(blockUserList[position])
    }


    inner class ViewHolder(val binding: ItemBlockUserBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(BlockUser: BlockUser){
            binding.itemBlockUserProfileCiv.setImageResource(BlockUser.profileImg!!)
            binding.itemBlockUserIconCiv.setImageResource(BlockUser.icon!!)
            binding.itemBlockUserBadgeIv.setImageResource(BlockUser.badge!!)
            binding.itemBlockUserNameTv.text=BlockUser.name
        }

    }
}