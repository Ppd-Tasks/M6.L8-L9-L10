package com.example.m6l8.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
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

class SignUpActivity : BaseActivity() {
    lateinit var edt_email: EditText
    lateinit var edt_pwd:EditText
    lateinit var edt_confirm:EditText
    lateinit var edt_name:EditText
    lateinit var tv_signup: TextView
    lateinit var btn_signup: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.navigationBarColor = resources.getColor(R.color.orange)

        initViews()
    }

    fun initViews() {
        edt_confirm = findViewById(R.id.edt_confirm)
        edt_name = findViewById(R.id.edt_name)
        edt_email = findViewById(R.id.edt_email)
        tv_signup = findViewById(R.id.tv_signin)
        edt_pwd = findViewById(R.id.edt_pwd)
        btn_signup = findViewById(R.id.btn_signup)



        btn_signup.setOnClickListener {
            val email = edt_email.text.toString().trim()
            val password = edt_pwd.text.toString().trim()

            if(!isValidEmail(email)){
                toast("ERROR INVALID EMAIL")
            }else if(!isValidPassword(password)){
                toast("ERROR INVALID PASSWORD")
            }else{
                firebaseSignUp(email,password)
            }

        }

        tv_signup.setOnClickListener {
            callSignInActivity()
        }
    }

    fun firebaseSignUp(email:String,password:String){
        showLoading(this)
        AuthManager.signUp(email,password,object : AuthHandler {
            override fun onSuccess() {
                dismissLoading()
                Toast.makeText(this@SignUpActivity, "Signed Up Successfully", Toast.LENGTH_SHORT).show()
                callMainActivity(context)
            }

            override fun onError(exception: Exception?, errorCode: String) {
                if (errorCode == "ERROR_EMAIL_ALREADY_IN_USE"){
                    toast("This credential is already associated with a different user account.")

                }
                dismissLoading()
                Toast.makeText(this@SignUpActivity, "Sign Up Failed", Toast.LENGTH_SHORT).show()
                Log.d("@@@", "onError: ${exception.toString()}")
            }

        })
    }

    fun callSignInActivity() {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }
}