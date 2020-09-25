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
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var signUpBtn:Button
    lateinit var signInBtn:Button

    private val mAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)



        signUpBtn = findViewById(R.id.signUpBtn)
        signInBtn = findViewById(R.id.signInBtn)



        signUpBtn.setOnClickListener(this)
        signInBtn.setOnClickListener(this)


    }

    private fun checkDetails():Boolean {
        val isEmail:Boolean = checkEmail()
        val isPassword:Boolean = checkPassword()

//        Toast.makeText(this,"isEmail $isEmail",Toast.LENGTH_SHORT).show()
//        Toast.makeText(this,"isPassword $isPassword",Toast.LENGTH_SHORT).show()

        return isEmail and isPassword
    }

    private fun checkPassword(): Boolean {
        val password = userSignUpPasswordEt.editText?.text.toString()
        val confirmPassword = userSignUpConfirmPasswordEt.editText?.text.toString()

        if(password.isNullOrEmpty() or confirmPassword.isNullOrEmpty())
            return false
        else if(password.compareTo(confirmPassword) != 0)
            return false
        return true
    }

    private fun checkEmail(): Boolean {
        val email = userSignUpEmailEt.editText?.text.toString()
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onClick(v: View) {

        when(v){
            signInBtn ->{
                openSignInActivity()
            }

            signUpBtn ->{

                if(!checkDetails()){
                    Toast.makeText(this,"Fill correct details",Toast.LENGTH_SHORT).show()
                }

                else{

                    signUpWithFirebase()

                }




            }
        }

    }

    private fun openSignInActivity() {
        val intent = Intent(this,
            LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun signUpWithFirebase() {
        signUpBtn.isEnabled = false
        signInBtn.isEnabled = false
        val email = userSignUpEmailEt.editText?.text.toString()
        val password = userSignUpPasswordEt.editText?.text.toString()


        mAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this) { task ->

                signUpBtn.isEnabled = true
                signInBtn.isEnabled = true

                if(task.isSuccessful){
                    Toast.makeText(this,"Succesfully Sign Up",Toast.LENGTH_SHORT).show()
                    openMainActivity()
                }else{
                    Toast.makeText(this,"Some error occurred",Toast.LENGTH_SHORT).show()
                }
                signUpBtn.isEnabled = true
                signInBtn.isEnabled = true



            }.addOnFailureListener {
                Toast.makeText(this,"Some exception occurred",Toast.LENGTH_SHORT).show()

            }





    }

    private fun openMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


}
