package com.example.favoriteplace

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.favoriteplace.databinding.FragmentCommunityFreeLatelyBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommunityFreeLatelyFragment : Fragment() {

    lateinit var binding: FragmentCommunityFreeLatelyBinding
    private var freeLatelyWriteData = ArrayList<Posts>()
    private var currentPage=1
    private var isLastPage=false
    private lateinit var adapter: CommunityFreeLatelyRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("실행이 되는가1","success")
        binding= FragmentCommunityFreeLatelyBinding.inflate(inflater,container,false)

        fetchPosts()

        return binding.root
    }

    private fun fetchPosts() {
        Log.d("실행이 되는가3","success")
        RetrofitClient.communityService.getPosts(currentPage,10,"latest")
            .enqueue(object : Callback<CommunityPost> {

                override fun onResponse(
                    call: Call<CommunityPost>,
                    response: Response<CommunityPost>
                ) {
                    Log.d("실행이 되는가4",currentPage.toString())
                    if (response.isSuccessful) {

                        Log.d("실행이 되는가5",response.body().toString())
                        if(response.body()?.post?.isNotEmpty() == true&&!isLastPage){
                            response.body()?.let { post ->

                                    freeLatelyWriteData.addAll(post.post.map { item ->
                                        Posts(
                                            item.id,
                                            item.title,
                                            item.nickname,
                                            item.views,
                                            item.likes,
                                            item.comments,
                                            item.passedTime
                                        )
                                    })
                                    Log.d("실행이 되는가7",freeLatelyWriteData.toString())
                                    currentPage++
                                    fetchPosts()
                                    val latelywriteRVAdapter =
                                        CommunityFreeLatelyRVAdapter(freeLatelyWriteData)
                                    binding.communityFreeLatelyRv.adapter = latelywriteRVAdapter
                                    binding.communityFreeLatelyRv.layoutManager =
                                        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

                                Log.d("실행이 되는가8",freeLatelyWriteData.toString())
                            }
                        } else {
                            isLastPage=true
                        }
                    }
                }
                override fun onFailure(call: Call<CommunityPost>, t: Throwable) {
                    Log.d("CommunityFreeLatelyFragment","Network Error: ${t.message}")
                }
            })
    }

}