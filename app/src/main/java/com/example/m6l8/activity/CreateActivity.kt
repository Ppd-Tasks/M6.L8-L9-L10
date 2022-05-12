package com.example.m6l8.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import com.example.m6l8.R
import com.example.m6l8.manager.DatabaseHandler
import com.example.m6l8.manager.DatabaseManager
import com.example.m6l8.manager.StorageHandler
import com.example.m6l8.manager.StorageManager
import com.example.m6l8.model.Post
import com.example.m6l8.utils.Extensions.toast
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter
import java.lang.Exception

class CreateActivity : BaseActivity() {
    lateinit var et_body:EditText
    lateinit var et_title:EditText
    lateinit var btn_create:Button
    lateinit var iv_close:ImageView

    lateinit var iv_camera:ImageView
    lateinit var iv_photo:ImageView
    var pickedPhoto:Uri? = null
    var allPhotos=ArrayList<Uri>()

    lateinit var mimeType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        initViews()
    }

    private fun initViews() {
        et_title = findViewById(R.id.et_title)
        et_body = findViewById(R.id.et_body)
        btn_create = findViewById(R.id.b_create)
        iv_close = findViewById(R.id.iv_close)
        iv_camera = findViewById(R.id.iv_camera)
        iv_photo = findViewById(R.id.iv_photo)

        iv_close.setOnClickListener {
            finish()
        }

        btn_create.setOnClickListener {
            val title = et_title.text.toString().trim()
            val body = et_body.text.toString().trim()
            val post = Post("",title, body,pickedPhoto.toString())
            storePost(post)
        }

        iv_photo.setOnClickListener {
            pickUserPhoto()
        }

        iv_camera.setOnClickListener {
            startCamera()
        }
    }

    fun storePost(post: Post){
        if (pickedPhoto != null){
            storeStorage(post)
        }else{
            storeDatabase(post)
        }
    }

    private fun startCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(intent)
    }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                toast("${it.data!!.data}")
            }
        }

    fun storeStorage(post: Post){
        showLoading(this)
        StorageManager.uploadPhotos(pickedPhoto!!, mimeType,object : StorageHandler{
            override fun onSuccess(imgUrl: String) {
                post.img = imgUrl
                storeDatabase(post)
            }

            override fun onError(exception: Exception?) {
                storeDatabase(post)
            }

        })
    }

    fun storeDatabase(post: Post) {
        DatabaseManager.storePost(post,object :DatabaseHandler{
            override fun onSuccess(post: Post?, posts: ArrayList<Post>) {
                Log.d("@@@", "onSuccess: Post is saved")
                dismissLoading()
                finishIntent()
            }

            override fun onError() {
                dismissLoading()
                Log.d("@@@", "onError: Post is not saved")
            }

        })
    }

    fun pickUserPhoto(){
        FishBun.with(this)
            .setImageAdapter(GlideAdapter())
            .setMaxCount(1)
            .setMinCount(1)
            .hasCameraInPickerPage(true)
            .setSelectedImages(allPhotos)
            .startAlbumWithActivityResultCallback(photoLauncher)
    }

    val photoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == Activity.RESULT_OK){
            allPhotos = it.data?.getParcelableArrayListExtra(FishBun.INTENT_PATH) ?: arrayListOf()
            pickedPhoto = allPhotos.get(0)
            mimeType = contentResolver.getType(pickedPhoto!!).toString()
            iv_photo.setImageURI(pickedPhoto)
        }
    }

    private fun finishIntent() {
        val returnIntent = intent
        setResult(RESULT_OK,returnIntent)
        finish()
    }


}