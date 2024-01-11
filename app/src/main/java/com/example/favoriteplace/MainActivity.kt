package com.example.favoriteplace

import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_post_detail)

        // 댓글 입력창의 참조를 얻습니다.
        val commentEditText = findViewById<EditText>(R.id.comment_register_et)

// 루트 뷰의 참조를 얻습니다.
        val rootView = findViewById<View>(android.R.id.content)

// 뷰 트리 옵저버를 사용하여 키보드의 상태를 감지합니다.
        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            rootView.getWindowVisibleDisplayFrame(rect)
            val screenHeight = rootView.height

            // 화면 높이와 가시 영역의 차이를 계산하여 키보드의 높이를 추정합니다.
            val keypadHeight = screenHeight - rect.bottom

            if (keypadHeight > screenHeight * 0.15) { // 키보드가 열려있음
                commentEditText.setPadding(0, 0, 0, keypadHeight) // 키보드 높이만큼 패딩 추가
            } else { // 키보드가 닫혀있음
                commentEditText.setPadding(0, 0, 0, 0) // 패딩 제거
            }
        }


//        setContentView(R.layout.fragment_rally_home_nonmember)
//
//        val interestedRallyLayout: ConstraintLayout = findViewById(R.id.interestedRally_cl)
//        val certificatedRallyLayout: ConstraintLayout = findViewById(R.id.certification_board_cl)
//
//        interestedRallyLayout.setOnClickListener {
//            val inflater = layoutInflater
//            val layout: View = inflater.inflate(R.layout.custom_toast, findViewById(R.id.custom_toast_message))
//
//            val textView = layout.findViewById<TextView>(R.id.custom_toast_message)
//            textView.text = "로그인 후 이용 가능한 메뉴입니다."
//
//            with (Toast(applicationContext)) {
//                setDuration(Toast.LENGTH_SHORT)
//                setView(layout)
//                show()
//            }
//        }
//
//        certificatedRallyLayout.setOnClickListener {
//            val inflater = layoutInflater
//            val layout: View = inflater.inflate(R.layout.custom_toast, findViewById(R.id.custom_toast_message))
//
//            val textView = layout.findViewById<TextView>(R.id.custom_toast_message)
//            textView.text = "로그인 후 이용 가능한 메뉴입니다."
//
//            with (Toast(applicationContext)) {
//                setDuration(Toast.LENGTH_SHORT)
//                setView(layout)
//                show()
//            }
//        }
    }


}