package com.example.tmk815.hacku_destiny

import android.content.*
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_note_detail.*
import java.io.File
import java.io.FileInputStream


class NoteDetailActivity : AppCompatActivity() {

    private var storage = FirebaseStorage.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_detail)

        val intent = intent

        var mManager: ClipboardManager =
            applicationContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        val database = FirebaseDatabase.getInstance()
        val userID = intent.getStringExtra("USERID")
        val paypayRef = database.getReference("user").child(userID)

        paypayRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (user in dataSnapshot.children) {
                    if (user.key.toString() == "paypayID") {
                        paypayIDTextView.textSize = 18F
                        paypayIDTextView.text = user.value.toString()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException() as Throwable?)
            }
        })

        val localFile = File.createTempFile("images", "jpg")
        val httpsReference = storage.getReferenceFromUrl(
            intent.getStringExtra("URI")
        )

        httpsReference.getFile(localFile).addOnSuccessListener {
            // Data for "images/island.jpg" is returned, use this as needed
            val fis = FileInputStream(localFile)
            val image = BitmapFactory.decodeStream(fis)
            noteImageView.setImageBitmap(image)
        }.addOnFailureListener {
            // Handle any errors
        }

        paypayIDTextView.setOnClickListener {
            //クリッピボードへのコピー
            mManager.primaryClip = ClipData.newPlainText("label", paypayIDTextView.text)

            val packageName = "jp.ne.paypay.android.app"
            val className = "jp.ne.paypay.android.app.MainActivity"
            val pm = this.packageManager
            var intent = pm.getLaunchIntentForPackage(packageName)

            intent?.also {
                it.setClassName(packageName, className)
            } ?: run {
                intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("market://details?id=$packageName")
            }
            startActivity(intent)
        }
    }
}
