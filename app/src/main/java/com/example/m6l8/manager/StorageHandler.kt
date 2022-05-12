package com.example.m6l8.manager

import java.lang.Exception

interface StorageHandler{
    fun onSuccess(imgUrl:String)
    fun onError(exception: Exception?)
}