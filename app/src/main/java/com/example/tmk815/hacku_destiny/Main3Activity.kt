package com.example.tmk815.hacku_destiny

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.activity_main3.*

class Main3Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        textView3.text = intent.getStringExtra("TEXT_KEY2")

        //ノート画像表示

        back3.setOnClickListener{
            val intent = Intent(this, Main2Activity::class.java)
            startActivity(intent)
        }
    }
}
