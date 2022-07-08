package co.fanavari.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import co.fanavari.myapplication.databinding.ActivityComponentBinding
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.snackbar.Snackbar

class ComponentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityComponentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityComponentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.topAppBar.setNavigationOnClickListener {
            // Handle navigation icon press
        }

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.favorite -> {
                    // Handle favorite icon press
                    showToast("favorite")
                    true
                }
                R.id.search -> {
                    // Handle search icon press
                    true
                }
                R.id.more -> {
                    // Handle more item (inside overflow menu) press
                    true
                }
                else -> false
            }
        }

        val bar = binding.bottomAppBar

        bar.setNavigationOnClickListener {
            // Handle navigation icon press

            if(bar.fabAlignmentMode == BottomAppBar.FAB_ALIGNMENT_MODE_CENTER)
                bar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
        }

        binding.bottomAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.search -> {
                    // Handle search icon press
                    true
                }
                R.id.more -> {
                    // Handle more item (inside overflow menu) press
                    true
                }
                else -> false
            }
        }

        binding.floatingActionButton.setOnClickListener {
            if(bar.fabAlignmentMode == BottomAppBar.FAB_ALIGNMENT_MODE_END)
                bar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
        }
        binding.checkboxLabel4.setOnClickListener {

            val checked: Boolean = binding.checkboxLabel4.isChecked

           if(checked){
               showToast("label 4 is checked")
               binding.radioButton2.isChecked = true
           }

        }

        binding.checkBoxLabel2.setOnClickListener {
            val checked: Boolean = binding.checkBoxLabel2.isChecked

            if(checked)
                Snackbar.make(binding.linearLayoutComponent,
                "Text lable",
                Snackbar.LENGTH_INDEFINITE)
                    .setAction("Action"){
                        //respond to click
                    }.show()
        }

        val checkedRadioButtonId = binding.radioGroup.checkedRadioButtonId // Returns View.NO_ID if nothing is checked.
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            // Responds to child RadioButton checked/unchecked
            when(checkedId){
                R.id.radio_button_1 -> showToast("radioButton 1")

            }
        }

// To check a radio button


// To listen for a radio button's checked/unchecked state changes
        binding.radioButton3.setOnCheckedChangeListener { buttonView, isChecked ->
            // Responds to radio button being checked/unchecked
        }
    }

    fun onCheckboxClicked(view: View){

    }
}