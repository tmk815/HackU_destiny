package com.example.tmk815.hacku_destiny

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_post.*
import java.io.ByteArrayOutputStream


class PostActivity : AppCompatActivity() {

    companion object {
        const val READ_REQUEST_CODE = 1
    }

    private var storageRef = FirebaseStorage.getInstance().reference
    val currentUser = FirebaseAuth.getInstance().currentUser
    val database = FirebaseDatabase.getInstance()
    var downloadUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        new_post_button.isEnabled = false

        note_picture_button.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)

            intent.type = "image/*"
            startActivityForResult(intent, READ_REQUEST_CODE)
        }

        new_post_button.setOnClickListener {
            val myRef =
                database.getReference("post").child("中部大学").child(subject.text.toString()).child(currentUser!!.uid)
                    .child(number.text.toString())
            var post = Post(Integer.parseInt(number.text.toString()), downloadUri.toString())
            myRef.setValue(post)
        }
    }

    @SuppressLint("MissingSuperCall")
    public override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Create a reference to "mountains.jpg"
            //val mountainsRef = storageRef.child("mountains.jpg")
            // Create a reference to 'images/mountains.jpg'
            val noteImagesRef = storageRef.child("images/${generateID(15)}.jpg")
            var uri: Uri?
            if (resultData != null) {
                uri = resultData.data
                val noteBitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                notePicture.setImageBitmap(noteBitmap)
                val bitmap = (notePicture.drawable as BitmapDrawable).bitmap
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()

                var uploadTask = noteImagesRef.putBytes(data)
                /*uploadTask.addOnFailureListener {
                    // Handle unsuccessful uploads
                    Toast.makeText(this, "失敗", Toast.LENGTH_SHORT)
                    Log.d("Miss", "失敗")
                }.addOnSuccessListener {
                    // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                    // ...
                    Toast.makeText(this, "成功", Toast.LENGTH_SHORT)
                }*/

                val urlTask = uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    return@Continuation noteImagesRef.downloadUrl
                }).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        downloadUri = task.result
                        Toast.makeText(this, "画像アップロード成功", Toast.LENGTH_SHORT).show()
                        new_post_button.isEnabled = true
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            }
        }
    }

    private fun generateID(size: Int): String {
        val source = "A1BCDEF4G0H8IJKLM7NOPQ3RST9UVWX52YZab1cd60ef2ghij3klmn49opq5rst6uvw7xyz8"
        return (source).map { it }.shuffled().subList(0, size).joinToString("")
    }
}
