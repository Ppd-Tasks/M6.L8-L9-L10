package com.example.m6l8.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.m6l8.R
import com.example.m6l8.adapter.PostAdapter
import com.example.m6l8.manager.AuthManager
import com.example.m6l8.manager.DatabaseHandler
import com.example.m6l8.manager.DatabaseManager
import com.example.m6l8.model.Post
import com.example.m6l8.utils.Extensions.toast
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : BaseActivity() {
    lateinit var fab_create: FloatingActionButton
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: PostAdapter
    private var list = ArrayList<Post>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.setLayoutManager(GridLayoutManager(this, 1))


        fab_create = findViewById(R.id.fab_create)
        fab_create.setOnClickListener {
            callCreateActivity()
        }

        val iv_logout: ImageView = findViewById(R.id.iv_logout)
        iv_logout.setOnClickListener {
            AuthManager.signOut()
            callSignInActivity(context)
        }


        apiLoadPosts()
    }

    fun refreshAdapter(list: ArrayList<Post>) {
        adapter = PostAdapter(this, list)
        recyclerView.adapter = adapter
    }

    fun dialogPost(post: Post){
        AlertDialog.Builder(this)
            .setTitle("Delete Poster")
            .setMessage("Are you sure you want to delete this poster?")
            .setPositiveButton(
                android.R.string.yes
            ){ dialog,which -> apiDeletePost(post) }
            .setNegativeButton(R.string.no){dialog,which -> callUpdateActivity(post)}
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }


    fun apiPostUpdate(key:String,post: Post){

        DatabaseManager.apiUpdatePost(key,post, object : DatabaseHandler{
            override fun onSuccess(post: Post?, posts: ArrayList<Post>) {
                apiLoadPosts()
            }

            override fun onError() {
                toast("Update Failed")
            }

        })
    }

    var resultLauncherUpdate = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data

            val post :Post = data!!.getSerializableExtra("post") as Post
            apiPostUpdate(post.id!!,post)

            //apiLoadPosts()
        }
    }

    fun apiLoadPosts() {
        showLoading(this)
        DatabaseManager.apiLoadPosts(object : DatabaseHandler {
            @SuppressLint("NotifyDataSetChanged")
            override fun onSuccess(post: Post?, posts: ArrayList<Post>) {
                dismissLoading()

                list.clear()
                list.addAll(posts)
                refreshAdapter(list)
                adapter.notifyDataSetChanged()
            }

            override fun onError() {
                dismissLoading()
            }
        })
    }

    fun apiDeletePost(post: Post) {
        DatabaseManager.apiDeletePost(post, object: DatabaseHandler{
            override fun onSuccess(post: Post?, posts: ArrayList<Post>) {
                apiLoadPosts()
            }

            override fun onError() {

            }
        })
    }

    fun callCreateActivity() {
        val intent = Intent(context, CreateActivity::class.java)
        resultLauncherCreate.launch(intent)
    }
    var resultLauncherCreate = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            // Load all posts...
            apiLoadPosts()
        }
    }

    private fun callUpdateActivity(post: Post) {
        val intent = Intent(context,UpdateActivity::class.java)
        intent.putExtra("post",post)
        resultLauncherUpdate.launch(intent)
    }




}