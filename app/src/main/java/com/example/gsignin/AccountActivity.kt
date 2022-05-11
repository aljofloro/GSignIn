package com.example.gsignin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth

class AccountActivity : AppCompatActivity() {

  private lateinit var mAuth: FirebaseAuth

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_account)

    mAuth = FirebaseAuth.getInstance()

    val currentUser = mAuth.currentUser

    val iv_photo = findViewById<ImageView>(R.id.iv_avatar)


    findViewById<TextView>(R.id.txt_id).text = currentUser?.uid
    findViewById<TextView>(R.id.txt_name).text = currentUser?.displayName
    findViewById<TextView>(R.id.txt_email).text = currentUser?.email

    Glide.with(this).load(currentUser?.photoUrl).into(iv_photo)


    findViewById<Button>(R.id.btn_logout).setOnClickListener {
      mAuth.signOut()
      val intent = Intent(this,LoginActivity::class.java)
      startActivity(intent)
      finish()
    }


  }
}