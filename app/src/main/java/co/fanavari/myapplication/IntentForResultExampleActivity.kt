package co.fanavari.myapplication

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import co.fanavari.myapplication.databinding.ActivityIntentForResultExampleBinding

class IntentForResultExampleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIntentForResultExampleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntentForResultExampleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSendResult.setOnClickListener {
            val intent = Intent()
            val message = binding.editTextResult.text.toString()
            intent.putExtra("message", message)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }


    }
}