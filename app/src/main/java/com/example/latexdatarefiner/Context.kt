package com.example.latexdatarefiner

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast

fun Context.openActivity(activityClass: Class<out Activity>) {
    val intent = Intent(this, activityClass)
    startActivity(intent)
}


private fun doToast(context: Context, message: String, length: Int) {
    if (context is Activity) {
        if (!context.isFinishing && !context.isDestroyed) {
            Toast.makeText(context, message, length).show()
        }
    } else {
        Toast.makeText(context, message, length).show()
    }
}


