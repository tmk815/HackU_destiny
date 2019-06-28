package com.example.tmk815.hacku_destiny

import android.content.ContentValues
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
                    val nameRef = database.getReference("user").child(userID.key.toString())
                    Log.d("uidString", userID.key.toString())
                    childData["URI"] = userID.child("notePictureUri").getValue(String::class.java)!!
                    nameRef.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            //childData["NAME"] = dataSnapshot.child("name").getValue(String::class.java)!!
                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Failed to read value
                            Log.w(ContentValues.TAG, "Failed to read value.", error.toException() as Throwable?)
                        }

                    })
                    noteData.add(childData)
                }

                val adapter = SimpleAdapter(
                    this@NoteListActivity,
                    noteData,
                    android.R.layout.simple_list_item_2,
                    arrayOf("URI"),
                    intArrayOf(android.R.id.text1)
                )
                note_list.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException() as Throwable?)
            }
        })
    }
}
