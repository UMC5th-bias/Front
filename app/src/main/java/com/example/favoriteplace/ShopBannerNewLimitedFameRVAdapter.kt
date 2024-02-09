package com.example.favoriteplace

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.persistableBundleOf
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.favoriteplace.databinding.ItemShopBannerNewFameBinding
import okhttp3.HttpUrl.Companion.toHttpUrl
import retrofit2.Response
import kotlin.coroutines.coroutineContext

class ShopBannerNewLimitedFameRVAdapter(private val limitedFameList: ArrayList<UnlimitedFame>):RecyclerView.Adapter<ShopBannerNewLimitedFameRVAdapter.ViewHolder>(){

    private lateinit var mContext: Context
//    private lateinit var mData:List<ShopData>

    //RVA에서 setOnClickListener을 쓸 수 있도록 하는 인터페이스
    interface MyItemClickListener{
        fun onItemClick()
    }

    //전달받은 리스너 객체를 저장하는 변수
    private lateinit var mItemClickListener: MyItemClickListener
    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener=itemClickListener
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ShopBannerNewLimitedFameRVAdapter.ViewHolder {
        val binding: ItemShopBannerNewFameBinding=ItemShopBannerNewFameBinding.inflate(
            LayoutInflater.from(viewGroup.context),viewGroup,false)

        Log.d("실행이 되는가","success")
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int=limitedFameList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind(limitedFameList[position])
//        holder.binding.itemShopBannerNewFameIv.loadImageFromUrl(
//            limitedFameList[position].titles?.get(position)?.itemList?.get(position)?.imageUrl
//        )
//        holder.binding.itemShopBannerNewFameTv.text=limitedFameList[position].titles?.get(position)?.itemList?.get(position)?.point.toString()

//        GlideModule()
//        val requestManager = Glide.with(mContext)
//        val requestBuilder = requestManager.load(mData[position])
//        requestBuilder.into(holder.adImg)
        holder.itemView.setOnClickListener{
            mItemClickListener.onItemClick()
        }
    }

    inner class ViewHolder(val binding: ItemShopBannerNewFameBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(limitedFame: LimitedFame){
//            binding.itemShopBannerNewFameIv.loadImageFromUrl(limitedFame.titles?.get(0)?.itemList?.get(0)?.imageUrl)
//            binding.itemShopBannerNewFameTv.text= limitedFame.titles?.get(0)?.itemList?.get(0)?.imageUrl
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addLimitedFame(limitedFame: ArrayList<LimitedFame>){
//        this.limitedFameList.clear()
//        this.limitedFameList.addAll(limitedFame)

        notifyDataSetChanged()
        Log.d("notifyDt",limitedFameList.toString())
    }

    fun ImageView.loadImageFromUrl(imageUrl: String?){
        val imageLoader=ImageLoader.Builder(this.context).componentRegistry{add(SvgDecoder(context))}.build()
        val imageRequest=ImageRequest.Builder(this.context)
            .crossfade(false)
            .data(imageUrl)
            .target(onSuccess = {result -> val bitmap=(result as BitmapDrawable).bitmap
                this.setImageBitmap(bitmap)},).build()

        imageLoader.enqueue(imageRequest)
    }

    fun preload(context: Context, url:String){
        Glide.with(mContext).load(url).preload(150,150)
    }
}