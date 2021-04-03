package com.android.membershipbusiness

import android.content.Context
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

fun Context.toast(message: String): Unit {
    Toast.makeText(this, message, Toast.LENGTH_SHORT ).show()
}
fun Context.toastLong(message: String): Unit {
    Toast.makeText(this, message, Toast.LENGTH_LONG ).show()
}

fun ProgressBar.show(){
    visibility = View.VISIBLE
}

fun ProgressBar.hide(){
    visibility = View.GONE
}

fun View.snackbar(message: String){
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).also { snackbar ->
        snackbar.setAction("Ok") {
            snackbar.dismiss()
        }
    }.show()
}