package com.example.tmk815.hacku_destiny

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var list = mutableListOf("USER NAME_1", "USER NAME_2")
        var adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list)

        button.setOnClickListener{
            if(editText != null){
                //該当する大学に所属するユーザー出力
                listView.adapter = adapter
            }
        }
        listView.setOnItemClickListener { adapterView, view, i, l ->
            val listing = view.findViewById<TextView>(android.R.id.text1)
            Toast.makeText(this, "Clicked: ${listing.text}", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, Main2Activity::class.java)
            intent.putExtra("TEXT_KEY", listing.text)
            startActivity(intent)
        }
    }
}
