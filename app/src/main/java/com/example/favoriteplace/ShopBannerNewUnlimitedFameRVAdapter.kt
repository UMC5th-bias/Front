package com.example.favoriteplace

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.favoriteplace.databinding.ItemShopBannerNewFameBinding

class ShopBannerNewUnlimitedFameRVAdapter (private val unlimitedFameList: ArrayList<UnlimitedFame>):RecyclerView.Adapter<ShopBannerNewUnlimitedFameRVAdapter.ViewHolder>() {

    //RVA에서 setOnClickListener을 쓸 수 있도록 하는 인터페이스
    interface MyItemClickListener {
        fun onItemUnlimitedFameClick()
    }

    //전달받은 리스너 객체를 저장하는 변수
    private lateinit var mItemClickListener: MyItemClickListener
    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener=itemClickListener
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ShopBannerNewUnlimitedFameRVAdapter.ViewHolder {
        val binding: ItemShopBannerNewFameBinding= ItemShopBannerNewFameBinding.inflate(
            LayoutInflater.from(viewGroup.context),viewGroup,false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int=unlimitedFameList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(unlimitedFameList[position])
        holder.itemView.setOnClickListener{
            mItemClickListener.onItemUnlimitedFameClick()
        }
    }

    inner class ViewHolder(val binding: ItemShopBannerNewFameBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(unlimitedFame: UnlimitedFame){
            try {
                val imageLoader = ImageLoader.Builder(binding.root.context)
                    .componentRegistry {
                        add(SvgDecoder(binding.root.context)) // SVG 이미지 처리를 위해 SvgDecoder 추가
                    }
                    .build()

                val imageRequest = ImageRequest.Builder(binding.root.context)
                    .crossfade(true)
                    .crossfade(300)  //애니메이션 처리
                    .data(unlimitedFame.fameImg)
                    .target(binding.itemShopBannerNewFameIv)  //해당 이미지뷰를 타겟으로 svg 삽입
                    .build()
                imageLoader.enqueue(imageRequest)

                binding.itemShopBannerNewFameTv.text = unlimitedFame.cost

            } catch (e: Exception) {
                Log.e("ViewHolder", "Error loading image: ${e.message}")
                e.printStackTrace()
            }
        }

    }
}