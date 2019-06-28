package com.example.tmk815.hacku_destiny

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.SimpleAdapter
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_list_note.*

class NoteListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_note)

        //val currentUser = FirebaseAuth.getInstance().currentUser
        val database = FirebaseDatabase.getInstance()

        val intent = intent
        val post = database.getReference("post").child("中部大学").child(intent.getStringExtra("SUBJECT"))
            .child(intent.getStringExtra("NUMBER"))
        post.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val noteData = ArrayList<Map<String, String>>()
                Log.d("dataSnapshot", dataSnapshot.childrenCount.toString())
                for (userID in dataSnapshot.children) {
                    var childData = HashMap<String, String>()
                    Log.d("uidString", userID.key.toString())
                    childData["NAME"] = userID.child("name").getValue(String::class.java)!!
                    childData["URI"] = userID.child("notePictureUri").getValue(String::class.java)!!
                    childData["USERID"] = userID.key.toString()
                    noteData.add(childData)
                }

                val adapter = SimpleAdapter(
                    this@NoteListActivity,
                    noteData,
                    android.R.layout.simple_list_item_2,
                    arrayOf("NAME"),
                    intArrayOf(android.R.id.text1)
                )
                note_list.adapter = adapter
                // 項目をタップしたときの処理
                note_list.setOnItemClickListener {parent, view, position, id ->
                    val map = noteData[position]
                    val intent = Intent(this@NoteListActivity, NoteDetailActivity::class.java)
                    //intent変数をつなげる(第一引数はキー，第二引数は渡したい変数)
                    intent.putExtra("URI",map["URI"])
                    intent.putExtra("NAME",map["NAME"])
                    intent.putExtra("USERID",map["USERID"])
                    //画面遷移を開始
                    startActivity(intent)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException() as Throwable?)
            }
        })
    }
}
