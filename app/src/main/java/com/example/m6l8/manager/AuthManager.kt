package com.example.m6l8.manager

import com.example.initialfirebaseapp.manager.AuthHandler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser

class AuthManager {
    companion object{
        val firebaseAuth:FirebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser:FirebaseUser? = firebaseAuth.currentUser

        fun isSignedIn():Boolean{
            return firebaseUser != null
        }

        fun signIn(email:String,password:String,handler:AuthHandler){
            firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->
                if (task.isSuccessful){
                    handler.onSuccess()
                }else{
                    val errorCode = (task.exception as FirebaseAuthException?)!!.errorCode
                    handler.onError(task.exception, errorCode)
                }
            }
        }

        fun signUp(email:String,password:String,handler:AuthHandler){
            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
                if (task.isSuccessful){
                    handler.onSuccess()
                }else{
                    val errorCode = (task.exception as FirebaseAuthException?)!!.errorCode
                    handler.onError(task.exception,errorCode)
                }
            }
        }

        fun signOut(){
            firebaseAuth.signOut()
        }
    }
}