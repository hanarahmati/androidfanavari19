package co.fanavari.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import co.fanavari.myapplication.databinding.ActivityComponentBinding
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.NonCancellable.cancel

class ComponentActivity : AppCompatActivity() ,AdapterView.OnItemSelectedListener {
    private lateinit var binding: ActivityComponentBinding
    private lateinit var planetsArray: Array<String>
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

            if (bar.fabAlignmentMode == BottomAppBar.FAB_ALIGNMENT_MODE_CENTER)
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

        planetsArray = resources.getStringArray(R.array.planets_array)

        binding.floatingActionButton.setOnClickListener {
            if (bar.fabAlignmentMode == BottomAppBar.FAB_ALIGNMENT_MODE_END)
                bar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
        }
        binding.checkboxLabel4.setOnClickListener {

            val checked: Boolean = binding.checkboxLabel4.isChecked

            if (checked) {
                showToast("label 4 is checked")
                binding.radioButton2.isChecked = true
            }

        }

        binding.checkBoxLabel2.setOnClickListener {
            val checked: Boolean = binding.checkBoxLabel2.isChecked

            if (checked)
                Snackbar.make(
                    binding.linearLayoutComponent,
                    "Text lable",
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAction("Action") {
                        //respond to click
                    }.show()
        }

        val checkedRadioButtonId =
            binding.radioGroup.checkedRadioButtonId // Returns View.NO_ID if nothing is checked.
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            // Responds to child RadioButton checked/unchecked
            when (checkedId) {
                R.id.radio_button_1 -> showToast("radioButton 1")

            }
        }

// To check a radio button


// To listen for a radio button's checked/unchecked state changes
        binding.radioButton3.setOnCheckedChangeListener { buttonView, isChecked ->
            // Responds to radio button being checked/unchecked
        }

        binding.toggleButton.addOnButtonCheckedListener { _, checkedId, isChecked ->
            // Respond to button selection
            if (isChecked)
                showToast(isChecked.toString())

            if (checkedId == R.id.button1toggle) {
                showToast("toggle 1 selected")
                binding.checkBoxLabel2.isChecked = false
                binding.checkboxLabel4.isEnabled = true
            }


            when (checkedId) {
                R.id.button1toggle -> {
                    // handle my rules
                    true
                }
                R.id.button2toggle -> {
                    // handle my rules
                    showToast(checkedId.toString())
                    true
                }
                R.id.button3toggle -> {
                    // handle my rules
                    true
                }
                else -> false
            }
        }


        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            // Responds to child RadioButton checked/unchecked
        }

        val spinner = binding.planetsSpinner
        ArrayAdapter.createFromResource(
            this,
            R.array.planets_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = this

        binding.chipGroupFilter.setOnCheckedChangeListener { group, checkedId ->

            val chip: Chip? = group.findViewById(checkedId)
            if (checkedId == R.id.chip3) {
                showToast("chip 3")
                binding.switchMaterial.isChecked = false
            }

        }

    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {

            val checked = view.isChecked

            when (view.getId()) {
                R.id.radio_female -> {
                    if (checked) {
                        binding.radioButton1.isChecked = false
                    }

                }

                R.id.radio_male ->
                    if (checked) {

                        MaterialAlertDialogBuilder(this)
                            .setTitle(resources.getString(R.string.title))
                            .setMessage(resources.getString(R.string.supporting_text))
                            .setNeutralButton(resources.getString(R.string.cancel)) { dialog, which ->
                                // Respond to neutral button press
                            }
                            .setNegativeButton(resources.getString(R.string.decline)) { dialog, which ->
                                // Respond to negative button press
                            }
                            .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                                // Respond to positive button press
                            }
                            .show()
                    }
            }
        }
    }


    override fun onItemSelected(p0: AdapterView<*>?, view: View?, pos: Int, id: Long) {

        showToast("item with id $id and position $pos selected")
        showToast("item ${planetsArray[pos]}")

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        //TODO("Not yet implemented")
    }

    fun onCheckboxClicked(view: View) {

    }


}