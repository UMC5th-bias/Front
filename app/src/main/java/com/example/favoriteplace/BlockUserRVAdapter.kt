package com.example.favoriteplace

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.favoriteplace.databinding.ItemBlockUserBinding
import com.example.favoriteplace.databinding.ItemShopBannerNewFameBinding

class BlockUserRVAdapter (private val userList: ArrayList<User>): RecyclerView.Adapter<BlockUserRVAdapter.ViewHolder>(){


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemBlockUserBinding=ItemBlockUserBinding.inflate(
            LayoutInflater.from(viewGroup.context),viewGroup,false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = userList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(userList[position])
    }


    inner class ViewHolder(val binding: ItemBlockUserBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(User: User){
            binding.itemBlockUserProfileCiv.setImageResource(User.profileImg!!)
            binding.itemBlockUserIconCiv.setImageResource(User.icon!!)
            binding.itemBlockUserBadgeIv.setImageResource(User.badge!!)
            binding.itemBlockUserNameTv.text=User.name
        }

    }
}