package com.example.m6l8.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.initialfirebaseapp.manager.AuthHandler
import com.example.m6l8.R
import com.example.m6l8.manager.AuthManager
import com.example.m6l8.utils.Extensions.toast
import java.lang.Exception

class SignInActivity : BaseActivity() {
    lateinit var edt_email: EditText
    lateinit var edt_pwd:EditText
    lateinit var tv_signup: TextView
    lateinit var btn_signin: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.navigationBarColor = resources.getColor(R.color.orange)
        initViews()
    }

    fun initViews() {
        edt_email = findViewById(R.id.edt_email)
        tv_signup = findViewById(R.id.tv_signup)
        edt_pwd = findViewById(R.id.edt_pwd)
        btn_signin = findViewById(R.id.btn_signin)



        btn_signin.setOnClickListener(View.OnClickListener {
            val email = edt_email.text.toString().trim()
            val password = edt_pwd.text.toString().trim()
                firebaseSignIn(email,password)
        })

        tv_signup.setOnClickListener(View.OnClickListener {
            callSignUpActivity()
        })
    }

    fun callSignUpActivity() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun firebaseSignIn(email:String,password:String){
        showLoading(this)
        AuthManager.signIn(email,password,object :AuthHandler{
            override fun onSuccess() {
                dismissLoading()
                Toast.makeText(this@SignInActivity, "Signed In Successfully", Toast.LENGTH_SHORT).show()
                callMainActivity(context)
            }

            override fun onError(exception: Exception?, errorCode: String) {
                when(errorCode){
                    "ERROR_INVALID_EMAIL" -> {
                        toast("The email address is badly formatted.")
                        edt_email.setError("The email address is badly formatted.")
                        edt_email.requestFocus()
                    }
                    "ERROR_WRONG_PASSWORD" ->{
                        toast("The password is invalid or the user does not have a password.")
                        edt_pwd.setError("password is incorrect")
                        edt_pwd.requestFocus()
                        edt_pwd.setText("")
                    }
                }
                dismissLoading()
                Toast.makeText(this@SignInActivity, "Sign In Failed", Toast.LENGTH_SHORT).show()
                Log.d("@@@", "onErrorSignIn: ${exception.toString()}")
            }

        })
    }
}