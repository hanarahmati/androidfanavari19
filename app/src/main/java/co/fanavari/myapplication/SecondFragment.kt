package co.fanavari.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import co.fanavari.myapplication.databinding.FragmentSecondBinding


class SecondFragment : Fragment() {


    private var _binding: FragmentSecondBinding? = null

    private val binding get() = _binding!!

    private val arg: SecondFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(layoutInflater, container,
        false)
        val view = binding.root

        binding.textViewFragment2.text = "fragment 2 with new nubmer ${arg.number}"
        // Inflate the layout for this fragment

        binding.textViewFragment2.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_secondFragment_to_firstFragment)
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}