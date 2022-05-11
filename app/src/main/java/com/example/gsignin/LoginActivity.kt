package com.example.gsignin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

  companion object{
    private const val RC_SIGN_IN = 100
  }

  private lateinit var mAuth: FirebaseAuth
  private lateinit var googleSignInClient: GoogleSignInClient

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)

    //Configuramos Google Sign In
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
      .requestIdToken(getString(R.string.default_web_client_id))
      .requestEmail()
      .build()
    googleSignInClient = GoogleSignIn.getClient(this,gso)

    //Configuración de Firebase
    mAuth = FirebaseAuth.getInstance()

    findViewById<Button>(R.id.btn_GSignIn).setOnClickListener {
      signIn()
    }

  }

  private fun signIn(){
    val signInIntent = googleSignInClient.signInIntent
    startActivityForResult(signInIntent, RC_SIGN_IN)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    if(requestCode == RC_SIGN_IN){
      val task = GoogleSignIn.getSignedInAccountFromIntent(data)
      val exception = task.exception

      if(task.isSuccessful){
        try{
          val account = task.getResult(ApiException::class.java)!!
          Log.d("AppGSIGN","firebaseWithGoogle:"+account.id)
          firebaseAuthWithGoogle(account.idToken!!)
        }catch (e: ApiException){
          Log.w("AppGSIGN","Autenticación sin éxito",e)
        }
      }else{
        Log.w("AppGSIGN",exception.toString())
      }
    }
  }

  private fun firebaseAuthWithGoogle(idToken:String){
    val credential = GoogleAuthProvider.getCredential(idToken,null)
    mAuth.signInWithCredential(credential)
      .addOnCompleteListener(this){task->
        if(task.isSuccessful){
          Log.d("AppGSIGN","credenciales aceptadas")
          val user = mAuth.currentUser
          val intent = Intent(this,AccountActivity::class.java)
          startActivity(intent)
          finish()
        }else{
          Log.w("AppGSIGN","credenciales rechazadas",task.exception)
        }
      }
  }

}