package co.fanavari.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class LifecyleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lifecyle)
        print("on create")
    }

    override fun onStart() {
        super.onStart()
        print("on start")
    }

    override fun onResume() {
        super.onResume()
        print("on resume")
    }

    override fun onStop() {
        super.onStop()
        print("on stop")
    }

    override fun onDestroy() {
        super.onDestroy()
        print("on destroy")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        print("on back pressed!")
    }

    private fun print(msg: String){
        Log.i("activity state", "activity state : $msg")
    }
}