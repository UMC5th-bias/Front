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