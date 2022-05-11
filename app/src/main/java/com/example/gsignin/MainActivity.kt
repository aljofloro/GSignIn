package com.example.gsignin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

  private lateinit var mAuth:FirebaseAuth

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    mAuth = FirebaseAuth.getInstance()

    val user = mAuth.currentUser

    Handler().postDelayed({
      if(user != null){
        val accountActivity = Intent(this, AccountActivity::class.java)
        startActivity(accountActivity)
        finish()
      }else{
        val loginActivity = Intent(this,LoginActivity::class.java)
        startActivity(loginActivity)
        finish()
      }
    }, 2000)

  }
}