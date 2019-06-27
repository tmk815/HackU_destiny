package com.example.tmk815.hacku_destiny

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_user.*

class UserAddActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)

        val currentUser = FirebaseAuth.getInstance().currentUser
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("user").child(currentUser!!.uid)


        button.setOnClickListener {
            var user = User(name.text.toString(), universityName.text.toString(), paypayID.text.toString())
            myRef.setValue(user)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
