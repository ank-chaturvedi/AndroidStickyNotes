package com.basics.androidstickynotes.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.basics.androidstickynotes.MainActivity
import com.basics.androidstickynotes.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var signInBtn:Button
    lateinit var signUpBtn:Button

    lateinit var email:String
    lateinit var password:String

    private val auth by lazy {
        FirebaseAuth.getInstance()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        signInBtn  = findViewById(R.id.signInBtn)
        signUpBtn  = findViewById(R.id.signUpBtn)

        if(auth.currentUser != null){
            openMainActivity()
        }



        signInBtn.setOnClickListener(this)
        signUpBtn.setOnClickListener(this)
    }

    private fun openMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onClick(v: View?) {
        when(v){

            signInBtn ->{


                signInUser()



            }
            signUpBtn ->{


                    openSignUpActivity()


            }



        }
    }

    private fun openSignUpActivity() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    private fun signInUser() {
        signUpBtn.isEnabled  = false
        signInBtn.isEnabled = false


        email = userEmailEt.editText?.text.toString()
        password = userPasswordEt.editText?.text.toString()


        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(this) {task ->
                if(task.isSuccessful){
                    openMainActivity()
                }else{
                    Toast.makeText(this,"Please enter Correct Details",Toast.LENGTH_SHORT).show()
                }

                signInBtn.isEnabled = true
                signUpBtn.isEnabled = true
            }
    }
}
