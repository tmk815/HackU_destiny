package com.example.tmk815.hacku_destiny

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main2.*

class Main2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        textView2.text = intent.getStringExtra("TEXT_KEY")

        var list = mutableListOf("国語", "数学", "英語", "社会")
        var adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list)
        listView2.adapter = adapter

        listView2.setOnItemClickListener { adapterView, view, i, l ->
            val listing = view.findViewById<TextView>(android.R.id.text1)
            Toast.makeText(this, "Clicked: ${listing.text}", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, Main2Activity::class.java)
            intent.putExtra("TEXT_KEY2", listing.text)
            startActivity(intent)
        }
    }
}
