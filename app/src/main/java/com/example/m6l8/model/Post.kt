package com.example.m6l8.model

import java.io.Serializable

class Post :Serializable{

    var id:String? = null
    var title:String? = null
    var body:String? = null
    var img:String = ""

    constructor(){}

    constructor(title:String, body:String){
        this.title = title
        this.body = body

    }

    constructor(id: String?, title: String?, body: String?,img:String) {
        this.id = id
        this.title = title
        this.body = body
        this.img = img
    }
}