package com.example.m6l8.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.example.m6l8.R
import com.example.m6l8.model.Post

class UpdateActivity : AppCompatActivity() {
    lateinit var et_body: EditText
    lateinit var et_title: EditText
    lateinit var btn_update: Button
    lateinit var iv_close: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        initViews()
    }

    private fun initViews() {
        et_title = findViewById(R.id.et_title)
        et_body = findViewById(R.id.et_body)
        btn_update = findViewById(R.id.b_update)
        iv_close = findViewById(R.id.iv_close)

        iv_close.setOnClickListener {
            finish()
        }

        val post = intent.getSerializableExtra("post") as Post

        et_title.setText(post.title)
        et_body.setText(post.body)

        btn_update.setOnClickListener {
            back(post)
        }
    }

    fun back(post: Post){
        post.title = et_title.text.toString()
        post.body = et_body.text.toString()
        val intent = Intent()

        intent.putExtra("post",post)

        setResult(RESULT_OK,intent)
        finish()
    }
}