package com.example.favoriteplace

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.favoriteplace.databinding.FragmentCommunityRallySearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//변수 값 지정
private var searchType=""
private var searchText=""
class CommunityRallySearchFragment : Fragment(), SortBottomSheetFragment.OnSortOptionSelectedListener{

    lateinit var binding: FragmentCommunityRallySearchBinding
    private var freeSearchData=ArrayList<GuestBook>()
    private var currentPage=1

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommunityRallySearchBinding.inflate(inflater,container,false)

        //서치 방식과 서치한 키워드를 반영
        binding.communityRallySortSearchTv.text=searchType
        binding.searchTextView.text = Editable.Factory.getInstance().newEditable(searchText)

        //서치 방식에 따라 변수명 변경
        when (searchType){
            "제목" -> {
                searchType="title"
            }
            "내용" -> {
                searchType="content"
            }
            "닉네임" -> {
                searchType="nickname"
            }
        }

        fetchPosts()

        //돌아가기 버튼을 클릭했을 때
        binding.communityRallySearchIb.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frameLayout, CommunityRallyFragment().apply {
                })
                .commitAllowingStateLoss()
        }

        //검색 바에서 엔터를 누르고 떼었을 때
        binding.searchTextView.setOnKeyListener { v, keyCode, event ->
            if(keyCode== KeyEvent.KEYCODE_ENTER && event.action== KeyEvent.ACTION_UP){
                searchText=binding.searchTextView.text.toString()
                goToSearch()    //프래그먼트를 다시 실행하는 함수
                return@setOnKeyListener true
            }
            false
        }

        //검색 바에서 검색 아이콘을 누르고 떼었을 때
        binding.searchTextView.setOnTouchListener { v, event ->
            val DRAWABLE_RIGHT=2
            if (event.action== MotionEvent.ACTION_UP){
                if (event.rawX>=(binding.searchTextView.right-binding.searchTextView.compoundDrawables[DRAWABLE_RIGHT].bounds.width())){
                    searchText=binding.searchTextView.text.toString()
                    goToSearch()    //프래그먼트를 다시 실행하는 함수
                    return@setOnTouchListener true
                }
            }
            false
        }

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //서치 타입과 서치 키워드를 받아오는 코드
        arguments?.let {
            searchType = it.getString("SEARCH_TYPE").toString()
            searchText = it.getString("SEARCH_TEXT").toString()
        }
    }

    //서버에서 검색 결과를 가져오는 코드
    private fun fetchPosts() {
        RetrofitClient.communityService.getRallySearch(currentPage,10, searchType, searchText)
            .enqueue(object : Callback<RallyPost> {

                override fun onResponse(
                    call: Call<RallyPost>,
                    response: Response<RallyPost>
                ) {
                    if (response.isSuccessful) {
                        if(response.body()?.guestBook?.isNotEmpty() == true){   //post의 값이 있을 경우,
                            response.body()?.let { post ->
                                //freeLatelyWriteData에 데이터를 받아옴
                                freeSearchData.addAll(post.guestBook.map { item ->
                                    GuestBook(
                                        item.id,
                                        item.title,
                                        item.nickname,
                                        item.thumbnail,
                                        item.views,
                                        item.likes,
                                        item.comments,
                                        item.passedTime,
                                        item.hashTags
                                    )
                                })

                                currentPage++   //다음 페이지를 받아오기 위해 현재 페이지를 1 증가 시킴
                                fetchPosts()    //재귀함수

                                //RVA실행
                                val latelywriteRVAdapter =
                                    CommunityRallyLatelyRVAdapter(freeSearchData, object : CommunityRallyLatelyRVAdapter.OnItemClickListener{
                                        override fun onItemClick(guestBookId: Long) {
                                            val intent = Intent(context, MyGuestBookActivity::class.java).apply {
                                                putExtra("GUESTBOOK_ID", guestBookId)
                                            }
                                            startActivity(intent)
                                        }
                                    })
                                binding.communityRallyLatelyRv.adapter = latelywriteRVAdapter
                                binding.communityRallyLatelyRv.layoutManager =
                                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<RallyPost>, t: Throwable) {
                    Log.d("CommunityFreeSearchFragment","Network Error: ${t.message}")
                }
            })
    }

    private fun goToSearch(){
        //서치 타입을 한글로 변경하는 코드
        when (searchType){
            "title" -> {
                searchType="제목"
            }
            "content" -> {
                searchType="내용"
            }
            "nickname" -> {
                searchType="닉네임"
            }
        }

        //키보드를 숨기는 코드
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.searchTextView.windowToken, 0)

        //프래그먼트를 다시 실행하는 함수
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frameLayout, CommunityFreeSearchFragment().apply {
                arguments = Bundle().apply {
                    putString("SEARCH_TEXT", searchText) // SEARCH_TEXT 키로 사용자가 검색한 내용 저장
                    putString("SEARCH_TYPE", searchType) // SEARCH_TYPE 키로 사용자가 입력한 서치 방식 저장
                }
            })
            .commitAllowingStateLoss()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //서치 시트 버튼을 클릭했을 때
        binding.communityRallySortSearchIb.setOnClickListener {
            modalWithRoundCorner()
        }
    }

    //서치 시트 모서리를 둥글게 만드는 코드
    private fun modalWithRoundCorner() {
        val modal = SortBottomSheetFragment().apply {
            setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundCornerBottomSheetDialogTheme)
        }
        modal.show(childFragmentManager, SortBottomSheetFragment.TAG)
    }
    override fun onSortOptionSelected(option: String) {
        when (option) {
            "제목" -> {
                // 제목으로 정렬하는 작업 수행
                binding.communityRallySortSearchTv.text="제목"
                searchType="제목"
            }
            "내용" -> {
                // 날짜로 정렬하는 작업 수행
                binding.communityRallySortSearchTv.text="내용"
                searchType="내용"
            }
            "닉네임" -> {
                // 닉네임으로 정렬하는 작업 수행
                binding.communityRallySortSearchTv.text="닉네임"
                searchType="닉네임"
            }
        }
    }
}