package com.example.tmk815.hacku_destiny

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_post.*
import java.io.IOException


class PostActivity : AppCompatActivity() {

    companion object {
        const val READ_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        val currentUser = FirebaseAuth.getInstance().currentUser
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("post").database.getReference("中部大学").database.getReference(currentUser!!.uid)

        note_picture_button.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)

            intent.type = "image/*"
            startActivityForResult(intent, READ_REQUEST_CODE)
        }

    }

    @SuppressLint("MissingSuperCall")
    public override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            var uri: Uri? = null
            if (resultData != null) {
                uri = resultData.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                    notePicture.setImageBitmap(bitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
    }
}
