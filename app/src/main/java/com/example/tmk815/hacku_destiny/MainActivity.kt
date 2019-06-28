package com.example.tmk815.hacku_destiny

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ExpandableListView
import android.widget.SimpleExpandableListAdapter
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var university: String? = null

        val currentUser = FirebaseAuth.getInstance().currentUser
        val database = FirebaseDatabase.getInstance()
        val user = database.getReference("user").child(currentUser!!.uid)

        user.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                university = dataSnapshot.child("univercity").getValue(String::class.java)
                Log.d("UNIVERCITY",university)
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException() as Throwable?)
            }

            })

        //val postRef = database.getReference("post").child(university!!)
        val postRef = database.getReference("post").child("中部大学")

        // Read from the database
        postRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                // グループの親項目用のリスト
                val parentList = ArrayList<Map<String, String>>()
                // 子要素全体用のリスト
                val allChildList = ArrayList<List<Map<String, String>>>()

                //RealTimeDBから取り出した内容をArrayListに追加
                for (subject in dataSnapshot.children) {
                    // 親リストに表示する内容を生成
                    val parentData = HashMap<String, String>()
                    parentData["SUBJECT"] = subject.key.toString()
                    // グループの親項目用のリストに内容を格納
                    parentList.add(parentData)

                    // 各グループ別のリスト項目用のリスト
                    val childList = ArrayList<Map<String, String>>()

                    for (number in subject.children) {
                        val childData = HashMap<String, String>()
                        childData["NUMBER"] = number.key.toString()

                        // リストに文字を格納
                        childList.add(childData)
                    }
                    // 子要素全体用のリストに各グループごとデータを格納
                    allChildList.add(childList)
                }
                // アダプタを作る
                val adapter = SimpleExpandableListAdapter(
                    this@MainActivity,
                    parentList,
                    android.R.layout.simple_expandable_list_item_1,
                    arrayOf("SUBJECT"),
                    intArrayOf(android.R.id.text1, android.R.id.text2),
                    allChildList,
                    android.R.layout.simple_expandable_list_item_1,
                    arrayOf("NUMBER"),
                    intArrayOf(android.R.id.text1, android.R.id.text2)
                )
                //生成した情報をセット
                //val lv = note_list
                note_list.setAdapter(adapter)

                //グループの子項目がクリックされた時
                note_list.setOnChildClickListener { parent, view, groupPosition, childPosition, id ->
                    val adapter = parent.expandableListAdapter
                    val number = adapter.getChild(groupPosition, childPosition) as Map<String, String>
                    // クリックされた場所の内容情報を取得
                    //val item = adapter.getChild(groupPosition, childPosition) as Map<String, String>
                    //val orderFlag = mutableMapOf<String, Any>()
                    //orderFlag["already"] = true
                    val subject = parentList[groupPosition]
                    //val number = allChildList[childPosition]
                    val intent = Intent(this@MainActivity, NoteListActivity::class.java)
                    //intent変数をつなげる(第一引数はキー，第二引数は渡したい変数)
                    intent.putExtra("SUBJECT",subject["SUBJECT"])
                    intent.putExtra("NUMBER",number["NUMBER"])
                    //画面遷移を開始
                    startActivity(intent)
                    false
                }

                // グループの親項目がクリックされた時の処理
                note_list.setOnGroupClickListener(ExpandableListView.OnGroupClickListener { parent, view, groupPosition, id ->

                    false
                })

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    // メニュー選択された時の処理
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_post -> {
                val intent = Intent(this, PostActivity::class.java)
                startActivity(intent)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    }
}
