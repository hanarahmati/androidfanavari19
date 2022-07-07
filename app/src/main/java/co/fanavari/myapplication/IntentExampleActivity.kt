package co.fanavari.myapplication

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock
import androidx.activity.result.contract.ActivityResultContracts
import co.fanavari.myapplication.databinding.ActivityIntentExampleBinding

class IntentExampleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIntentExampleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntentExampleBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val bundle: Bundle? = intent.extras

        bundle?.let {
            val mentorName = "${bundle.getString(Constant.MENTOR_NAME)} class number " +
                    "${bundle.getInt("class Number")}"

            binding.textViewClassInfo.text = mentorName
            showToast(mentorName)

        }

        val openIntentForResultExampleActivity =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                if( it.resultCode == Activity.RESULT_OK)
                    showToast(it.data?.getStringExtra("message").toString())
                else
                    showToast("empty data")
            }

        binding.buttonStartActForRes.setOnClickListener{
            openIntentForResultExampleActivity.launch(
                Intent(this, IntentForResultExampleActivity::class.java).apply {
                    putExtra("id", "1")
                }
            )
        }

        binding.buttonOpenUrl.setOnClickListener {
            openUrl()
        }

        binding.buttonCreateAlarm.setOnClickListener {
            createAlarm("my alarm", 2, 32)
        }

        binding.buttonSendEmail.setOnClickListener {
            composeEmail(arrayOf("hana.rahmati@gmail.com", "fanavari.co@gmail.com"),
            "contact with us")
        }

        binding.buttonLifeCycle.setOnClickListener {
            val intent = Intent(this, LifecyleActivity::class.java)
            startActivity(intent)
        }
    }

    private fun openUrl(){
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://fanavari.co/android-course/"))
        startActivity(intent)
    }

    private fun createAlarm(message: String, hour: Int, minutes: Int) {
        val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
            putExtra(AlarmClock.EXTRA_MESSAGE, message)
            putExtra(AlarmClock.EXTRA_HOUR, hour)
            putExtra(AlarmClock.EXTRA_MINUTES, minutes)
        }
//        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
//        }
    }

    private fun composeEmail(addresses: Array<String>, subject: String){
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, addresses)
            putExtra(Intent.EXTRA_SUBJECT, subject)
        }
        //if(intent.resolveActivity(packageManager) != null){
            startActivity(intent)
       // }
    }
}