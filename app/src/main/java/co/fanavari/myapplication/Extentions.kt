package co.fanavari.myapplication

import android.content.Context
import android.widget.Toast
import java.time.Duration

fun Context.showToast(msg: String, duration: Int = Toast.LENGTH_LONG){
    Toast.makeText(this,msg, duration).show()
}