package co.fanavari.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import co.fanavari.myapplication.coroutines.CoroutinesActivity
import co.fanavari.myapplication.databinding.ActivityDashboardBinding
import co.fanavari.myapplication.huawei.HuaweiPushActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.CoroutineScope

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_dashboard)

        binding = ActivityDashboardBinding.inflate(layoutInflater)

        setContentView(binding.root)

        /*val backButton: AppCompatImageButton = findViewById(R.id.backButton)
        val logOutButton: AppCompatImageButton = findViewById(R.id.logOutButton)

        val todoButton: MaterialButton = findViewById(R.id.todoButton)
        val layoutCard: MaterialCardView = findViewById(R.id.layoutCards)

        layoutCard.setOnClickListener {
            Toast.makeText(this,"layout card is clicked!", Toast.LENGTH_LONG).show()
        }*/

        binding.layoutCards.setOnClickListener {
            showToast("this is my new toast from layout card")
        }

        binding.editProfileButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.themeCard.setOnClickListener {
            val intent = Intent(this, IntentExampleActivity::class.java)
            val mentorName: String = binding.textViewMentorName.text.toString()

            intent.putExtra("class Number", 7)
            intent.putExtra(Constant.MENTOR_NAME, mentorName)

            startActivity(intent)

        }
        binding.layoutCards.setOnClickListener {
            val intent = Intent(this, ComponentActivity::class.java)
            startActivity(intent)
        }

        binding.archCards.setOnClickListener {
            val intent = Intent(this, NavigationComponentExampleActivity::class.java)
            startActivity(intent)
        }

        binding.internetCards.setOnClickListener {
            val intent = Intent(this, CoroutinesActivity::class.java)
            startActivity(intent)
        }

        binding.todoButton.setOnClickListener {
            val intent = Intent(this, HuaweiPushActivity::class.java)
            startActivity(intent)
        }
    }
}